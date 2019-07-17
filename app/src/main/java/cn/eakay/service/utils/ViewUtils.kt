package cn.eakay.service.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.ListView

/**
 * @packageName: UserService
 * @fileName: ViewUtils
 * @author: chitian
 * @date: 2019-07-11 15:37
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
object ViewUtils {
    /**
     * 两次点击间隔不能少于1000ms
     */
    private val MIN_DELAY_TIME = 1000
    private var lastClickTime: Long = 0

    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter
            ?: // pre-condition
            return

        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
    }

    fun showKeyBoard(focusedView: View?) {
        if (null == focusedView) {
            return
        }
        val inputManager = focusedView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(focusedView, 0)
    }

    fun closeKeyBoard(focusedView: View?) {
        if (null == focusedView) {
            return
        }
        focusedView.clearFocus()
        val inputManager = focusedView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
    }

    fun addOnGlobalLayoutListener(view: View?, runner: Runnable?): ViewTreeObserver.OnGlobalLayoutListener? {
        if (null == view) {
            return null
        }

        val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                removeOnGlobalLayoutListener(view, this)
                runner?.run()
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
        return layoutListener
    }

    @SuppressLint("NewApi")
    fun removeOnGlobalLayoutListener(view: View?, listener: ViewTreeObserver.OnGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view!!.viewTreeObserver.removeGlobalOnLayoutListener(listener)
        } else {
            view!!.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    /**
     * 判断是否是快速点击
     *
     * @return
     */
    @Synchronized
    fun isFastClick(): Boolean {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime >= MIN_DELAY_TIME) {
            flag = false
        }
        lastClickTime = currentClickTime
        return flag
    }
    /**
     * 是否在后台运行
     *
     * @param context
     * @return
     */
    fun isBackground(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND
            }
        }
        return false
    }
}