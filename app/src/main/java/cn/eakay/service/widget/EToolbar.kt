package cn.eakay.service.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.appcompat.widget.Toolbar
import cn.eakay.service.R

/**
 * @packageName: UserService
 * @fileName: EToolbar
 * @author: chitian
 * @date: 2019-07-11 15:40
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class EToolbar : Toolbar {
    private var mTitleTextAppearance: Int = 0
    private var mTitleTextColor: Int = 0

    private var mTitleViewAtCenter: TextView? = null

    private var drawableTitleLeft: Drawable? = null
    private var drawableTitleRight: Drawable? = null
    private var drawablePadding: Int = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.eToolbarStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.EToolbar, defStyleAttr, 0)
        drawableTitleLeft = a.getDrawable(R.styleable.EToolbar_android_drawableLeft)
        drawableTitleRight = a.getDrawable(R.styleable.EToolbar_android_drawableRight)
        drawablePadding = a.getInt(R.styleable.EToolbar_drawablePadding, 0)
        setTitleDrawable(drawableTitleLeft, drawableTitleRight)

        mTitleTextAppearance = a.getResourceId(R.styleable.EToolbar_titleTextAppearance, 0)
        if (mTitleTextAppearance != 0) {
            setTitleTextAppearance(context, mTitleTextAppearance)
        }

        val menuResId = a.getResourceId(R.styleable.EToolbar_menu, -1)
        if (menuResId != -1) {
            inflateMenu(menuResId)
        }

        a.recycle()
    }

    override fun setTitle(title: CharSequence) {
        super.setTitle("")
        if (!TextUtils.isEmpty(title)) {
            if (mTitleViewAtCenter == null) {
                ensureCenterTitle()
            }

            if (!isChild(mTitleViewAtCenter!!)) {
                addView(mTitleViewAtCenter)
            }
        } else if (mTitleViewAtCenter != null && isChild(mTitleViewAtCenter!!)) {
            removeView(mTitleViewAtCenter)
        }
        if (mTitleViewAtCenter != null) {
            mTitleViewAtCenter!!.text = title
        }
    }

    private fun isChild(child: View): Boolean {
        return child.parent === this
    }

    override fun setTitleTextAppearance(context: Context, @StyleRes resId: Int) {
        super.setTitleTextAppearance(context, 0)
        mTitleTextAppearance = resId
        if (mTitleViewAtCenter != null) {
            mTitleViewAtCenter!!.setTextAppearance(context, resId)
        }
    }

    override fun setTitleTextColor(@ColorInt color: Int) {
        super.setTitleTextColor(0)
        mTitleTextColor = color
        if (mTitleViewAtCenter != null) {
            mTitleViewAtCenter!!.setTextColor(color)
        }
    }

    fun setCenterTitleOnClickListener(lis: OnClickListener) {
        ensureCenterTitle()
        mTitleViewAtCenter!!.setOnClickListener(lis)
    }

    private fun ensureCenterTitle() {
        if (null == mTitleViewAtCenter) {
            mTitleViewAtCenter = TextView(context)
            mTitleViewAtCenter!!.gravity = Gravity.CENTER
            mTitleViewAtCenter!!.setSingleLine()
            mTitleViewAtCenter!!.ellipsize = TextUtils.TruncateAt.END
            val lp = Toolbar.LayoutParams(-2, -1, Gravity.CENTER)
            mTitleViewAtCenter!!.layoutParams = lp

            if (mTitleTextAppearance != 0) {
                mTitleViewAtCenter!!.setTextAppearance(context, mTitleTextAppearance)
            }
            if (mTitleTextColor != 0) {
                mTitleViewAtCenter!!.setTextColor(mTitleTextColor)
            }

            if (drawableTitleLeft != null || drawableTitleRight != null) {
                setTitleDrawable(drawableTitleLeft, drawableTitleRight)
            }
        }
    }

    fun setTitleDrawable(leftDrawable: Drawable?, rightDrawable: Drawable?) {
        if (mTitleViewAtCenter != null) {
            mTitleViewAtCenter!!.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, rightDrawable, null)
            if (drawablePadding != 0) {
                mTitleViewAtCenter!!.compoundDrawablePadding = drawablePadding
            }
        }
    }
}