package cn.eakay.service.utils

import android.content.Context
import android.os.Build
import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * @packageName: UserService
 * @fileName: RomUtils
 * @author: chitian
 * @date: 2019-07-11 09:14
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
object RomUtils {
    internal object AvailableRomType {
        const val MIUI = 1
        const val FLYME = 2
        const val ANDROID_NATIVE = 3
        const val NA = 4
    }

    fun isLightStatusBarAvailable(): Boolean {
        return isMIUIV6OrAbove() || isFlymeV4OrAbove() || isAndroidMOrAbove()
    }

    fun getLightStatausBarAvailableRomType(): Int {
        if (isMIUIV6OrAbove()) {
            return AvailableRomType.MIUI
        }

        if (isFlymeV4OrAbove()) {
            return AvailableRomType.FLYME
        }

        return if (isAndroidMOrAbove()) {
            AvailableRomType.ANDROID_NATIVE
        } else AvailableRomType.NA

    }

    //Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
    //Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
    private fun isFlymeV4OrAbove(): Boolean {
        val displayId = Build.DISPLAY
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            val displayIdArray = displayId.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (temp in displayIdArray) {
                //版本号4以上，形如4.x.
                if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*".toRegex())) {
                    return true
                }
            }
        }
        return false
    }

    //MIUI V6对应的versionCode是4
    //MIUI V7对应的versionCode是5
    private fun isMIUIV6OrAbove(): Boolean {
        val miuiVersionCodeStr = getSystemProperty("ro.miui.ui.version.code")
        if (!TextUtils.isEmpty(miuiVersionCodeStr)) {
            try {
                val miuiVersionCode = Integer.parseInt(miuiVersionCodeStr!!)
                if (miuiVersionCode >= 4) {
                    return true
                }
            } catch (e: Exception) {
            }

        }
        return false
    }

    //Android Api 23以上
    private fun isAndroidMOrAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                }

            }
        }
        return line
    }

    fun checkIsExitApp(context: Context, pkhName: String): Boolean {
        // 获取packagemanager
        val packageManager = context.packageManager
        // 获取所有已安装程序的包信息
        val pinfo = packageManager.getInstalledPackages(0)
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo[i].packageName
                if (pn.equals(pkhName, ignoreCase = true)) {
                    return true
                }

            }
        }
        return false
    }
}