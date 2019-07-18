package cn.eakay.service.utils

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import cn.eakay.service.base.EakayApplication

/**
 * @packageName: UserService
 * @fileName: ToastUtils
 * @author: chitian
 * @date: 2019-07-09 15:33
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
object ToastUtils {
    private var toast: Toast? = null
    fun showShort(msg: Any) {
        when (msg) {
            is String -> showToast(msg)
            is Int -> showToast(msg)
        }
    }

    private fun showToast(msg: String) {
        Looper.prepare()
        if (toast == null) {
            toast = Toast.makeText(EakayApplication.instance!!, msg, Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.apply { show() }
        Looper.loop()
    }

    private fun showToast(msg: Int) {
        Looper.prepare()
        if (toast == null) {
            toast = Toast.makeText(EakayApplication.instance!!, msg, Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.apply { show() }
        Looper.loop()
    }

    fun showToast(context: Context, msg: String) {
        Looper.prepare()
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.apply { show() }
        Looper.loop()
    }

    fun showToast(context: Context, msg: Int) {
        Looper.prepare()
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.apply { show() }
        Looper.loop()
    }
}