package cn.eakay.service.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import cn.eakay.service.R
import com.github.florent37.viewanimator.ViewAnimator
import com.nineoldandroids.view.ViewHelper

/**
 * @packageName: UserService
 * @fileName: EakaySlideToUnLockView
 * @author: chitian
 * @date: 2019-07-12 09:18
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class EakaySlideToUnLockView : RelativeLayout {
    private var mActionDownX: Int = 0
    private var mLastX:Int = 0
    private var mSlidedDistance:Int = 0

    private val TAG = "EakaySlideToUnLockView"
    /** 左弹回,动画时长 */
    private val DEAFULT_DURATIN_LONG: Long = 200
    /** 右弹,动画时长 */
    private val DEAFULT_DURATIN_SHORT: Long = 100
    /** 打印开关 */
    private val LOG = true
    /** 滑动阈值 */
    private var DISTANCE_LIMIT = 600
    /** 滑动阈值比例:默认是0.5,即滑动超过父容器宽度的一半再松手就会触发 */
    private var THRESHOLD = 0.5f
    protected var mContext: Context? = null
    /** 滑块 */
    private var iv_slide: ImageView? = null
    /** 提示文本 */
    private var tv_hint: TextView? = null
    /** 滑动view */
    private var rl_slide: RelativeLayout? = null
    /** 父容器 */
    private var rl_root: RelativeLayout? = null
    /** 已经滑到最右边,将不再响应touch事件 */
    private var mIsUnLocked: Boolean = false
    /** 回调 */
    private var mCallBack: CallBack? = null

    /** 滑块宽度 */
    private var slideImageViewWidth: Int = 0
    /** 滑块资源 */
    private var slideImageViewResId: Int = 0
    /** 滑动到右边时,滑块资源id */
    private var slideImageViewResIdAfter: Int = 0
    /** root 背景 */
    private var viewBackgroundResId: Int = 0
    /** 文本 */
    private var textHint: String? = null
    /** 单位是sp,只拿数值 */
    private var textSize: Int = 0
    /** 颜色,@color */
    private var textColorResId: Int = 0


    constructor(mContext: Context) : super(mContext) {
        this.mContext = mContext
        initView()
    }

    constructor(mContext: Context, attrs: AttributeSet) : super(mContext, attrs) {

        this.mContext = mContext
        val mTypedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SlideToUnlockView)
        init(mTypedArray)
        initView()
    }

    constructor(mContext: Context, attrs: AttributeSet, defStyleAttr: Int) : super(mContext, attrs, defStyleAttr) {
        this.mContext = mContext
        val mTypedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SlideToUnlockView)
        init(mTypedArray)
        initView()
    }

    /**
     * @param :[mTypedArray]
     * @return type:void
     * @method name:init
     * @des:获取自定义属性
     */
    private fun init(mTypedArray: TypedArray) {
        slideImageViewWidth =
            mTypedArray.getDimension(R.styleable.SlideToUnlockView_slideImageViewWidth, dp2px(context, 50f).toFloat())
                .toInt()
        slideImageViewResId = mTypedArray.getResourceId(R.styleable.SlideToUnlockView_slideImageViewResId, -1)
        slideImageViewResIdAfter = mTypedArray.getResourceId(R.styleable.SlideToUnlockView_slideImageViewResIdAfter, -1)
        viewBackgroundResId = mTypedArray.getResourceId(R.styleable.SlideToUnlockView_viewBackgroundResId, -1)
        textHint = mTypedArray.getString(R.styleable.SlideToUnlockView_textHint)
        textSize = mTypedArray.getInteger(R.styleable.SlideToUnlockView_textSize, 7)
        textColorResId = mTypedArray.getColor(
            R.styleable.SlideToUnlockView_textColorResId,
            resources.getColor(android.R.color.white)
        )
        THRESHOLD = mTypedArray.getFloat(R.styleable.SlideToUnlockView_slideThreshold, 0.5f)
        mTypedArray.recycle()
    }





    /**
     * 初始化界面布局
     */
    @SuppressLint("ClickableViewAccessibility")
    protected fun initView() {

        LayoutInflater.from(mContext).inflate(R.layout.layout_slide_to_unlock_view, this, true)
        rl_root = findViewById<RelativeLayout>(R.id.rl_root)
        rl_slide = findViewById<RelativeLayout>(R.id.rl_slide)
        iv_slide = findViewById<ImageView>(R.id.iv_slide)
        tv_hint = findViewById<TextView>(R.id.tv_hint)
        val params = iv_slide!!.layoutParams as RelativeLayout.LayoutParams
        //获取当前控件的布局对象
        params.width = slideImageViewWidth//设置当前控件布局的高度
        iv_slide!!.layoutParams = params//将设置好的布局参数应用到控件中
        setImageDefault()
        if (viewBackgroundResId > 0) {
            rl_slide!!.setBackgroundResource(viewBackgroundResId)//rootView设置背景
        }
        val tvParams = tv_hint!!.layoutParams as ViewGroup.MarginLayoutParams
        tvParams.setMargins(0, 0, slideImageViewWidth, 0)//textview的marginRight设置为和滑块的宽度一致
        tv_hint!!.layoutParams = tvParams
        tv_hint!!.textSize = sp2px(context, textSize.toFloat()).toFloat()
        tv_hint!!.setTextColor(textColorResId)
        tv_hint!!.text =
            if (TextUtils.isEmpty(textHint)) mContext!!.getString(R.string.swipe_off_work_and_log_out) else textHint
        //添加滑动监听
        rl_slide!!.setOnTouchListener(OnTouchListener { v, event ->
            DISTANCE_LIMIT = (this@EakaySlideToUnLockView.width * THRESHOLD).toInt()//默认阈值是控件宽度的一半

            when (event.action) {

                MotionEvent.ACTION_DOWN//按下时记录纵坐标
                -> {

                    if (mIsUnLocked) {//滑块已经在最右边则不处理touch
                        return@OnTouchListener false
                    }

                    mLastX = event.rawX.toInt()//最后一个action时x值
                    mActionDownX = event.rawX.toInt()//按下的瞬间x
                    logI(TAG, "$mLastX X,=============================ACTION_DOWN")
                }

                MotionEvent.ACTION_MOVE//上滑才处理,如果用户一开始就下滑,则过掉不处理
                -> {

                    logI(TAG, "=============================ACTION_MOVE")
                    logI(TAG, "event.getRawX()=============================" + event.rawX)

                    val dX = event.rawX.toInt() - mLastX
                    logI(TAG, "dX=============================$dX")

                    mSlidedDistance = event.rawX.toInt() - mActionDownX
                    logI(TAG, "mSlidedDistance=============================$mSlidedDistance")

                    val params = v.layoutParams as ViewGroup.MarginLayoutParams
                    val left = params.leftMargin
                    val top = params.topMargin
                    val right = params.rightMargin
                    val bottom = params.bottomMargin

                    logI(TAG, "left:$left,top:$top,right:$right,bottom$bottom")

                    val leftNew = left + dX
                    val rightNew = right - dX

                    if (mSlidedDistance > 0) {//直接通过margin实现滑动
                        params.setMargins(leftNew, top, rightNew, bottom)
                        logI(TAG, "$leftNew  =============================MOVE")
                        v.layoutParams = params
                        resetTextViewAlpha(mSlidedDistance)

                        //回调
                        if (mCallBack != null) {
                            mCallBack!!.onSlide(mSlidedDistance)
                        }
                        mLastX = event.rawX.toInt()
                    } else {
                        return@OnTouchListener true
                    }
                }


                MotionEvent.ACTION_UP -> {
                    logI(TAG, "MotionEvent.ACTION_UP,之前移动的偏移值：" + ViewHelper.getTranslationY(v))
                    if (Math.abs(mSlidedDistance) > DISTANCE_LIMIT) {
                        scrollToRight(v)//右边
                    } else {
                        scrollToLeft(v)//左边
                    }
                }


                MotionEvent.ACTION_CANCEL -> {
                }
                else -> {
                }
            }
            true
        })


    }


    private fun logI(tag: String, content: String) {
        if (LOG) {
            Log.i(tag, content)
        }
    }

    /**
     * @param :[mSlidedDistance]
     * @return type:void
     * @method name:resetTextViewAlpha
     * @des: 重置提示文本的透明度
     */
    private fun resetTextViewAlpha(distance: Int) {

        if (Math.abs(distance) >= Math.abs(DISTANCE_LIMIT)) {
            tv_hint!!.alpha = 0.0f
        } else {
            tv_hint!!.alpha = 1.0f - Math.abs(distance) * 1.0f / Math.abs(DISTANCE_LIMIT)
        }
    }


    /**
     * @param :[v]
     * @return type:void
     * @method name:scrollToLeft
     * @des: 滑动未到阈值时松开手指, 弹回到最左边
     */
    private fun scrollToLeft(v: View) {


        val params1 = v.layoutParams as ViewGroup.MarginLayoutParams
        logI(TAG, "scrollToLeft,ViewHelper.getTranslationX(v)：" + ViewHelper.getTranslationX(v))
        logI(TAG, "scrollToLeft，params1.leftMargin：" + params1.leftMargin)
        logI(TAG, "scrollToLeft， params1.rightMargin：" + params1.rightMargin)


        ViewAnimator
            .animate(rl_slide)
            .translationX(ViewHelper.getTranslationX(v), (-params1.leftMargin).toFloat())
            .interpolator(AccelerateInterpolator())
            .duration(DEAFULT_DURATIN_LONG)
            .onStop {
                val para = v.layoutParams as ViewGroup.MarginLayoutParams
                logI(TAG, "scrollToLeft动画结束para.leftMargin：" + para.leftMargin)
                logI(TAG, "scrollToLeft动画结束para.rightMargin：" + para.rightMargin)
                logI(TAG, "scrollToLeft动画结束,ViewHelper.getTranslationX(v):" + ViewHelper.getTranslationX(v))
                mSlidedDistance = 0
                tv_hint!!.alpha = 1.0f
                mIsUnLocked = false
                if (mCallBack != null) {
                    mCallBack!!.onSlide(mSlidedDistance)
                }
                setImageDefault()
            }
            .start()
    }


    /**
     * @param :[v]
     * @return type:void
     * @method name:scrollToRight
     * @des:滑动到右边,并触发回调
     * @date 创建时间:2017/5/24
     * @author Chuck
     */
    private fun scrollToRight(v: View) {


        val params1 = v.layoutParams as ViewGroup.MarginLayoutParams
        logI(TAG, "scrollToRight,ViewHelper.getTranslationX(v)：" + ViewHelper.getTranslationX(v))
        logI(TAG, "scrollToRight，params1.leftMargin：" + params1.leftMargin)
        logI(TAG, "scrollToRight， params1.rightMargin：" + params1.rightMargin)

        //移动到最右端  移动的距离是 父容器宽度-leftMargin
        ViewAnimator
            .animate(rl_slide)
            .translationX(
                ViewHelper.getTranslationX(v),
                (rl_slide!!.width - params1.leftMargin - slideImageViewWidth).toFloat()
            )
            //.translationX(params1.leftMargin, ( rl_slide.getWidth() - params1.leftMargin-100))
            .interpolator(AccelerateInterpolator())
            .duration(DEAFULT_DURATIN_SHORT)
            .onStop {
                val para = v.layoutParams as ViewGroup.MarginLayoutParams
                logI(TAG, "scrollToRight动画结束para.leftMargin：" + para.leftMargin)
                logI(TAG, "scrollToRight动画结束para.rightMargin：" + para.rightMargin)
                logI(TAG, "scrollToRight动画结束,ViewHelper.getTranslationX(v):" + ViewHelper.getTranslationX(v))
                mSlidedDistance = 0
                tv_hint!!.alpha = 0.0f
                mIsUnLocked = true

                if (slideImageViewResIdAfter > 0) {
                    iv_slide!!.setImageResource(slideImageViewResIdAfter)//滑块imagview设置资源
                }

                //回调
                if (mCallBack != null) {
                    mCallBack!!.onUnlocked()
                }
            }
            .start()


    }


    fun resetView() {
        mIsUnLocked = false
        setImageDefault()
        scrollToLeft(rl_slide!!)
    }

    private fun setImageDefault() {
        /**
         * @method name:setImageDefault
         * @des: 设置默认图片
         * @param :[]
         * @return type:void
         * @date 创建时间:2017/5/25
         * @author Chuck
         */

        if (slideImageViewResId > 0) {
            iv_slide!!.setImageResource(slideImageViewResId)//滑块imagview设置资源
        }
    }

    interface CallBack {
        fun onSlide(distance: Int) //右滑距离回调

        fun onUnlocked() //滑动到了右边,事件回调
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal, context.resources.displayMetrics
        ).toInt()
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }

    fun getmCallBack(): CallBack? {
        return mCallBack
    }

    fun setmCallBack(mCallBack: CallBack) {
        this.mCallBack = mCallBack
    }
}