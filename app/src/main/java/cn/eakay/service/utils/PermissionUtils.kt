package cn.eakay.service.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.shs.easywebviewsupport.utils.LogUtils

/**
 * @packageName: UserService
 * @fileName: PermissionUtils
 * @author: chitian
 * @date: 2019-07-11 16:33
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
object PermissionUtils {
    val TAG = "PermissionUtils"

    fun checkCamera(): Boolean {
        var canUse = true
        var mCamera: Camera? = null
        try {
            mCamera = Camera.open()
        } catch (e: Exception) {
            canUse = false
        }

        if (canUse && mCamera != null) {
            mCamera.release()
            mCamera = null
        }
        return canUse
    }

    fun checkWriteExternalStorage(ctx: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) === PackageManager.PERMISSION_GRANTED
    }

    fun checkReadExternalStorage(ctx: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) === PackageManager.PERMISSION_GRANTED
    }

    fun checkSmsPermissions(ctx: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.READ_SMS
        ) === PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.RECEIVE_SMS
        ) === PackageManager.PERMISSION_GRANTED
    }

    fun hasPermissions(context: Context, vararg perms: String): Boolean {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w(TAG, "hasPermissions: API version < M, returning true by default")
            return true
        }

        for (perm in perms) {
            val hasPerm = ContextCompat.checkSelfPermission(context, perm) === PackageManager.PERMISSION_GRANTED
            if (!hasPerm) {
                return false
            }
        }

        return true
    }

    /**
     * 获取所有的权限
     */
    fun getAllPermission(context: Context): Array<String>? {
        try {
            val pm = context.packageManager
            val pack = pm.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            val permissionStrings = pack.requestedPermissions
            for (i in permissionStrings.indices) {
                LogUtils.loge("----所有权限:" + permissionStrings[i])
            }
            return permissionStrings
        } catch (e: Exception) {
            return null
        }

    }
}