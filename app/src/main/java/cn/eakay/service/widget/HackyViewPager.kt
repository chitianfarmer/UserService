package cn.eakay.service.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * @packageName: UserService
 * @fileName: HackyViewPager
 * @author: chitian
 * @date: 2019-07-12 09:25
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class HackyViewPager :ViewPager{
    private var isLocked: Boolean = false
    constructor(context: Context):super(context){
        isLocked = false
    }
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
        isLocked = false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                return false
            }

        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return !isLocked && super.onTouchEvent(event)
    }

    fun toggleLock() {
        isLocked = !isLocked
    }

    fun setLocked(isLocked: Boolean) {
        this.isLocked = isLocked
    }

    fun isLocked(): Boolean {
        return isLocked
    }
}