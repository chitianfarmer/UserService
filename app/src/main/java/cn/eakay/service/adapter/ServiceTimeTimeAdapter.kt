package cn.eakay.service.adapter

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.response.TimeCheckBean
import java.util.*

/**
 * @packageName: UserService
 * @fileName: ServiceTimeTimeAdapter
 * @author: chitian
 * @date: 2019-11-27 11:12
 * @description: 二次服务时间的时间选择器
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class ServiceTimeTimeAdapter : RecyclerView.Adapter<ServiceTimeTimeAdapter.TimeHolder> {
    private var context: Context
    private var beanList: MutableList<TimeCheckBean.TimeDayBeans.TimeTimeBeans>? = ArrayList()
    private var parentSize = Constants.NUMBER_ZERO
    private var listener: OnServiceTimeTimeClickListener? = null

    constructor(
        context: Context,
        beanList: MutableList<TimeCheckBean.TimeDayBeans.TimeTimeBeans>?,
        size: Int
    ) {
        this.beanList = beanList
        this.context = context
        this.parentSize = size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeHolder {
        val view = View.inflate(context, R.layout.item_time_time, null)
        return TimeHolder(view)
    }

    override fun getItemCount(): Int =
        if (beanList.isNullOrEmpty()) Constants.NUMBER_ZERO else beanList!!.size

    override fun onBindViewHolder(holder: TimeHolder, position: Int) {
        val timeBeans = beanList!![position]
        /*是否可用*/
        val flag = timeBeans.flag
        /*时间*/
        val time = timeBeans.time
        /*是否被旋球*/
        val checked = timeBeans.isChecked
        holder.tvTime.text = if (flag) time else ""
        /*设置是否可以点击*/
        holder.tvTime.isEnabled = flag
        /*设置选中与非选中的背景*/
        holder.tvTime.setBackgroundColor(if (checked) ContextCompat.getColor(context,R.color.base_activity_bg) else ContextCompat.getColor(context,R.color.white) )
        /*改变字体大小*/
        changeTextSize(holder)
        /*点击监听*/
        holder.itemView.setOnClickListener {
            run {
                if (listener != null) {
                    if (flag) {
                        listener!!.onTimeClicked(position,timeBeans)
                    }
                }
            }
        }
    }

    class TimeHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTime: TextView = view.findViewById(R.id.tv_time)
    }

    interface OnServiceTimeTimeClickListener {
        fun onTimeClicked(position:Int,bean: TimeCheckBean.TimeDayBeans.TimeTimeBeans)
    }

    fun setOnServiceTimeTimeClickListener(listener: OnServiceTimeTimeClickListener) {
        this.listener = listener
    }
    /**
     * 动态设置字体大小
     * @param holder
     */
    private fun changeTextSize(holder: TimeHolder) {
        val size = parentSize
        when {
            size <= Constants.NUMBER_FIVE -> {
                holder.tvTime.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    12f
                )
            }
            size == Constants.NUMBER_SIX -> {
                holder.tvTime.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    10f
                )
            }
            else -> {
                holder.tvTime.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    8f
                )
            }
        }
    }
}