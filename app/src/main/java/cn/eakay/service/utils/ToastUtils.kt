package cn.eakay.service.utils

import android.content.Context
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

     fun showToast(msg: String) {
        if (toast == null) {
            toast = Toast.makeText(EakayApplication.instance!!, msg, Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.apply { show() }
    }

     fun showToast(msg: Int) {
        if (toast == null) {
            toast = Toast.makeText(EakayApplication.instance!!, msg, Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.apply { show() }
    }
    fun showToast(context: Context,msg: String) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.apply { show() }
    }

    fun showToast(context: Context,msg: Int) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.apply { show() }
    }
}