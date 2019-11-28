package cn.eakay.service.adapter

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.response.TimeCheckBean
import com.shs.easywebviewsupport.utils.LogUtils

/**
 * @packageName: UserService
 * @fileName: ServiceTimeDayAdapter
 * @author: chitian
 * @date: 2019-11-27 10:34
 * @description: 服务时间的日期选择器
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class ServiceTimeDayAdapter : RecyclerView.Adapter<ServiceTimeDayAdapter.ViewHolder> {
    private var context: Context
    private var beanList: MutableList<TimeCheckBean.TimeDayBeans>? = ArrayList()
    private var listener: OnServiceTimeClickListener? = null

    constructor(context: Context, beanList: MutableList<TimeCheckBean.TimeDayBeans>?) {
        this.context = context
        this.beanList = beanList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.item_time_day, null)
        val holder = ViewHolder(view)
        holder.recyclerView.layoutManager = GridLayoutManager(context, Constants.NUMBER_ONE)
        holder.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                GridLayoutManager.VERTICAL
            )
        )
        return holder

    }

    override fun getItemCount(): Int = if (beanList.isNullOrEmpty()) 0 else beanList!!.size

    override fun onBindViewHolder(holder: ViewHolder, dayPosition: Int) {
        val dayBeans = beanList!![dayPosition]
        /*日期*/
        val titleDate = dayBeans.titleDate
        /*星期*/
        val week = dayBeans.week
        /*小时的时间集合*/
        val timeList = dayBeans.subscribeInfoVoList
        holder.tvDay.text = titleDate
        holder.tvWeek.text = String.format(context.getString(R.string.week), week)
        /*改变字体大小*/
        changeTextSize(holder)
        /*设置时间选择器*/
        val timeAdapter = ServiceTimeTimeAdapter(context, timeList, itemCount)
        holder.recyclerView.adapter = timeAdapter
        timeAdapter.setOnServiceTimeTimeClickListener(object :
            ServiceTimeTimeAdapter.OnServiceTimeTimeClickListener {
            override fun onTimeClicked(
                position: Int,
                bean: TimeCheckBean.TimeDayBeans.TimeTimeBeans
            ) {
                /*改变选择的条目*/
                changeCheckedTime(dayPosition, position)
                /*传递选中的日期*/
                if (listener != null) {
                    listener?.onTimeClicked(dayBeans, bean)
                }
            }

        })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay = view.findViewById<TextView>(R.id.tv_day)
        val tvWeek = view.findViewById<TextView>(R.id.tv_week)
        val viewTemp = view.findViewById<View>(R.id.view_temp)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
    }

    interface OnServiceTimeClickListener {
        fun onTimeClicked(
            dayBean: TimeCheckBean.TimeDayBeans?,
            timeBean: TimeCheckBean.TimeDayBeans.TimeTimeBeans?
        )
    }

    fun setOnServiceTimeClickListener(listener: OnServiceTimeClickListener) {
        this.listener = listener
    }

    /**
     * 更新点击的数据
     *
     * @param dayPosition
     * @param timePosition
     */
    private fun changeCheckedTime(dayPosition: Int, timePosition: Int) {
        for (i in beanList!!.indices) {
            val bean: TimeCheckBean.TimeDayBeans = beanList!![i]
            val list: MutableList<TimeCheckBean.TimeDayBeans.TimeTimeBeans>? =
                bean.subscribeInfoVoList
            bean.isChecked = false

            if (i == dayPosition) {
                bean.isChecked = true
            }
            for (j in list!!.indices) {
                val listBean: TimeCheckBean.TimeDayBeans.TimeTimeBeans = list[j]
                listBean.isChecked = false
                if (j == timePosition && bean.isChecked) {
                    listBean.isChecked = true
                }
            }
        }
        notifyDataSetChanged()
    }

    /**
     * 动态设置字体大小
     *
     * @param holder
     */
    private fun changeTextSize(holder: ViewHolder) {
        val size = itemCount
        when {
            size <= Constants.NUMBER_FIVE -> {
                holder.tvDay.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    12f
                )
                holder.tvWeek.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    10f
                )
            }
            else -> {
                holder.tvDay.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    10f
                )
                holder.tvWeek.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    8f
                )
            }
        }
    }
}