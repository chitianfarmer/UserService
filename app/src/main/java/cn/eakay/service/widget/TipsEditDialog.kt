package cn.eakay.service.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import cn.eakay.service.R
import cn.eakay.service.utils.ScreenUtils

/**
 * @packageName: UserService
 * @fileName: TipsEditDialog
 * @author: chitian
 * @date: 2019-11-27 13:37
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class TipsEditDialog : Dialog {
    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    open class Builder {
        private var context: Context
        private var title: String? = null
        private var positiveButtonText: String? = null
        private var negativeButtonText: String? = null
        private var positiveTextColor: Int = -1
        private var negativeTextColor: Int = -1
        private var canceledOnTouchOutside: Boolean = true
        private var listener: OnDialogClickListener? = null


        constructor(context: Context) {
            this.context = context
        }

        /**
         * 设置标题
         */
        fun setTitle(title: Any?): Builder {
            if (title is String) {
                this.title = title
            } else if (title is Int) {
                this.title = context!!.getString(title)
            }
            return this
        }

        /**
         * 设置是否可以外部点击失效
         */
        fun setCanceledOnTouchOutSide(status: Boolean): Builder {
            this.canceledOnTouchOutside = status
            return this
        }

        /**
         * 设置确定按钮的文案
         */
        fun setPositiveButton(msg: Any?): Builder {
            if (msg is String) {
                this.positiveButtonText = msg
            } else if (msg is Int) {
                this.positiveButtonText = context.getString(msg)
            }
            return this
        }

        /**
         * 设置确定按钮的颜色
         */
        fun setPositiveTextColor(@ColorInt color: Int): Builder {
            this.positiveTextColor = color
            return this
        }

        /**
         * 设置取消按钮的文案
         */
        fun setNegativeButton(msg: Any?): Builder {
            if (msg is String) {
                this.negativeButtonText = msg
            } else if (msg is Int) {
                this.negativeButtonText = context.getString(msg)
            }
            return this
        }

        /**
         * 设置取消按钮的颜色
         */
        fun setNegativeTextColor(@ColorInt color: Int): Builder {
            this.negativeTextColor = color
            return this
        }

        /**
         * 设置点击事件
         */
        fun setOnDialogClickListener(listener: OnDialogClickListener): Builder {
            this.listener = listener
            return this
        }

        /**
         * 创建对象
         */
        fun create(): TipsEditDialog {
            val inflater = LayoutInflater.from(context)
            val dialog = TipsEditDialog(context, R.style.Dialog)
            val view = inflater.inflate(R.layout.alert_dialog_edit, null)
            val tvTitle = view.findViewById<TextView>(R.id.tv_title)
            val dialogContent = view.findViewById<EditText>(R.id.dialog_content)
            val confirm = view.findViewById<TextView>(R.id.dialog_confirm)
            val cancel = view.findViewById<TextView>(R.id.dialog_cancel)
            tvTitle.text =
                if (title.isNullOrEmpty())
                    context.getString(R.string.fill_in_the_reason_for_the_inability_to_serve)
                else
                    title
            confirm.text =
                if (positiveButtonText.isNullOrEmpty())
                    context.getString(R.string.dialog_positive_button_text)
                else
                    positiveButtonText
            cancel.text =
                if (negativeButtonText.isNullOrEmpty())
                    context.getString(R.string.dialog_negative_button_text)
                else
                    negativeButtonText
            if (positiveTextColor != -1) {
                confirm.setTextColor(positiveTextColor)
            }
            if (negativeTextColor != -1) {
                cancel.setTextColor(negativeTextColor)
            }
            val content = dialogContent.text.toString().trim()

            if (listener!=null){
                confirm.setOnClickListener {
                    listener!!.onConfirmClick(dialog,content,DialogInterface.BUTTON_POSITIVE)
                }
                cancel.setOnClickListener {
                    listener!!.onCancelClick(dialog,DialogInterface.BUTTON_NEGATIVE)
                }
            }
            val window = dialog.window!!
            val rect = Rect()
            window.decorView.getWindowVisibleDisplayFrame(rect)
            view.minimumWidth = ScreenUtils.getScreenWidth(context)
            window.setGravity(Gravity.CENTER)
            dialog.setContentView(view)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
            return dialog
        }

    }

    /**
     * call back of dialog btn
     */
    interface OnDialogClickListener {
        fun onConfirmClick(
            dialog: Dialog?,
            content: String?,
            which: Int
        )

        fun onCancelClick(dialog: Dialog?, which: Int)
    }
}