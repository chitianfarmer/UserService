package cn.eakay.service.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import cn.eakay.service.R

/**
 * @packageName: UserService
 * @fileName: LoadDialog
 * @author: chitian
 * @date: 2019-11-27 15:25
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class LoadDialog : Dialog, View.OnClickListener {
    private var listener: DialogInterface.OnCancelListener? = null
    companion object{
        var animationDrawable: AnimationDrawable? = null
    }

    constructor(context: Context) : super(context, R.style.dialog_router)

    override fun onStart() {
        super.onStart()
        if (animationDrawable != null) {
            animationDrawable!!.start()
        }
    }

    override fun dismiss() {
        if (isShowing) {
            super.dismiss()
        }
        if (animationDrawable != null) {
            animationDrawable!!.stop()
        }
    }
    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        super.setOnCancelListener(listener)
        this.listener = listener
    }

    override fun onClick(v: View?) {
        if (listener != null) {
            listener!!.onCancel(this)
        }
    }

    open class Builder {
        private var context: Context
        private var message: Any? = null

        constructor(context: Context) {
            this.context = context
        }

        fun setMessage(message: Any?): Builder {
            this.message = message
            return this
        }

        fun create(): LoadDialog {
            val inflater = LayoutInflater.from(context)
            val dialog = LoadDialog(context)
            val view = inflater.inflate(R.layout.loading_dialog, null)
            val llDialog = view.findViewById<LinearLayout>(R.id.ll_dialog)
            val loadingImageView = view.findViewById<ImageView>(R.id.iv_loading)
            val tvMessage = view.findViewById<TextView>(R.id.tv_message)
            dialog.addContentView(
                view,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
            llDialog.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            loadingImageView!!.setImageResource(R.drawable.loading)
            if (message == null) {
                tvMessage!!.visibility = GONE
                tvMessage.text = context.getString(R.string.loading)
            } else {
                tvMessage!!.visibility = VISIBLE
                when (message!!) {
                    is String -> {
                        tvMessage.text = message as String
                    }
                    is Int -> {
                        tvMessage.text = context.getString(message as Int)
                    }
                    is CharSequence -> {
                        tvMessage.text = message as CharSequence
                    }
                }
            }
            animationDrawable = loadingImageView.drawable as AnimationDrawable?
            val window = dialog.window
            window!!.setGravity(Gravity.CENTER)
            dialog.setContentView(view)
            return dialog
        }
    }
}