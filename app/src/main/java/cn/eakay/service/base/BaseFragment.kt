package cn.eakay.service.base

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cn.eakay.service.R
import cn.eakay.service.widget.LoadDialog
import cn.eakay.service.widget.CommonDialog
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * @packageName: UserService
 * @fileName: BaseFragment
 * @author: chitian
 * @date: 2019-07-09 16:37
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
abstract class BaseFragment : Fragment() {
    private var loadDialog: LoadDialog? = null

    /**
     * 申请请求的request code
     */
    private val PERMISSION_REQUEST = 1200
    /**
     * 是否跳转过应用程序信息详情页
     */
    private var mIsJump2Settings = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindView()
        initData()
        setListener()
    }

    abstract fun getLayoutId(): Int
    abstract fun bindView()
    abstract fun initData()
    abstract fun setListener()

    fun showProgress() {
        val builder = LoadDialog.Builder(activity!!)
        if (loadDialog ==null){
            loadDialog = builder.create()
            loadDialog!!.window!!.setWindowAnimations(R.style.dialog_router)
        }
        if (!activity!!.isFinishing && !loadDialog!!.isShowing) {
            loadDialog!!.show()
        }
    }

    fun closeProgress() {
        if (loadDialog == null) {
            return
        }
        try {
            loadDialog!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        loadDialog = null
    }

    override fun onResume() {
        super.onResume()
        if (mIsJump2Settings) {
            onRecheckPermission()
            mIsJump2Settings = false
        }
    }

    open fun initEvent() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    open fun removeEvent() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    /**
     * 跳转到应用程序信息详情页面
     */
    open fun jump2PermissionSettings() {
        mIsJump2Settings = true
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${activity!!.packageName}")
        startActivity(intent)
    }

    // ----------------------权限检测 不要插队----------------------

    /**
     * 单个权限的检查
     */
    fun checkPermission(@NonNull permission: String, @Nullable reason: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        val permissionCheck = ContextCompat.checkSelfPermission(activity!!, permission)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //权限已经申请
            onPermissionGranted(permission)

        } else {
            if (!TextUtils.isEmpty(reason)) {
                //判断用户先前是否拒绝过该权限申请，如果为true，我们可以向用户解释为什么使用该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)) {
                    //这里的dialog可以自定义
                    val builder = CommonDialog.Builder(activity!!)
                    builder.setMessage(reason)
                    builder.setGravity(CommonDialog.Builder.MESSAGE_CENTER_GRAVITY)
                    builder.setPositiveButton(R.string.i_know)
                    builder.setOnDialogClickListener(object : CommonDialog.OnDialogClickListener {
                        override fun onConfirmClick(dialog: Dialog?, which: Int) {
                            dialog?.dismiss()
                            requestPermission(arrayOf(permission))
                        }

                        override fun onCancelClick(dialog: Dialog?, which: Int) {
                            dialog?.dismiss()
                        }
                    })
                    val mDialog = builder.create()
                    mDialog.setCancelable(false)
                    mDialog.show()
                } else {
                    requestPermission(arrayOf(permission))
                }
            } else {
                requestPermission(arrayOf(permission))
            }

        }
    }

    /**
     * 多个权限的检查
     */
    fun checkPermissions(@NonNull vararg permissions: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        //用于记录权限申请被拒绝的权限集合
        val permissionDeniedList = ArrayList<String>()
        for (permission in permissions) {
            val permissionCheck = ContextCompat.checkSelfPermission(activity!!, permission)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission)
            } else {
                permissionDeniedList.add(permission)
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            val deniedPermissions = permissionDeniedList.toTypedArray()
            requestPermission(deniedPermissions)
        }
    }

    /**
     * 调用系统API完成权限申请
     */
    open fun requestPermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity!!, permissions, PERMISSION_REQUEST)
    }

    /**
     * 申请权限被允许的回调
     */
    open fun onPermissionGranted(permission: String) {

    }

    /**
     * 申请权限被拒绝的回调
     */
    open fun onPermissionDenied(permission: String) {

    }

    /**
     * 申请权限的失败的回调
     */
    open fun onPermissionFailure() {

    }

    /**
     * 如果从设置界面返回，则重新申请权限
     */
    open fun onRecheckPermission() {

    }

    /**
     * 弹出系统权限询问对话框，用户交互后的结果回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST -> if (grantResults.isNotEmpty()) {
                //用于记录是否有权限申请被拒绝的标记
                var isDenied = false
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        onPermissionGranted(permissions[i])
                    } else {
                        isDenied = true
                        onPermissionDenied(permissions[i])
                    }
                }
                if (isDenied) {
                    isDenied = false
                    //如果有权限申请被拒绝，则弹出对话框提示用户去修改权限设置。
                    showPermissionSettingsDialog(permissions, grantResults)
                }

            } else {
                onPermissionFailure()
            }
            else -> {
            }
        }
    }

    open fun showPermissionSettingsDialog(@NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        val builder = CommonDialog.Builder(activity!!)
        builder.setMessage(R.string.lack_of_necessary_permissions)
        builder.setGravity(CommonDialog.Builder.MESSAGE_CENTER_GRAVITY)
        builder.setNegativeButton(R.string.dialog_negative_button_text)
        builder.setPositiveButton(R.string.go_to_set)
        builder.setOnDialogClickListener(object : CommonDialog.OnDialogClickListener {
            override fun onConfirmClick(dialog: Dialog?, which: Int) {
                dialog?.dismiss()
                jump2PermissionSettings()
            }

            override fun onCancelClick(dialog: Dialog?, which: Int) {
                dialog?.dismiss()
            }

        })
        val mDialog = builder.create()
        mDialog.setCancelable(false)
        mDialog.show()
    }
}