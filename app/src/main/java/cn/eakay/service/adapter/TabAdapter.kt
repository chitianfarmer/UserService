package cn.eakay.service.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.eakay.service.R
import com.alibaba.fastjson.JSONObject
import java.util.*

/**
 * @packageName: UserService
 * @fileName: TabAdapter
 * @author: chitian
 * @date: 2019-07-12 13:28
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class TabAdapter : RecyclerView.Adapter<TabAdapter.ItemHolder> {
    private var context: Context
    private var list: MutableList<JSONObject>
    private var listener: OnItemClickListener? = null

    constructor(context: Context, list: MutableList<JSONObject>) {
        this.context = context
        this.list = list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = View.inflate(context, R.layout.item_tab_list, null)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val bean = list[position]
//        /*订单类型*/
//        val type = bean.getOrderType()
//        /*订单编号*/
//        val orderNumber = bean.getOrderNumber()
//        /*订单状态*/
//        val orderStatus = bean.getOrderStatus()
//        /*订单地址*/
//        val address = bean.getCourrentAddress()
//        /*订单时间*/
//        val time = bean.getServiceTime()
        /*订单类型*/
        val type = "1"
        /*订单编号*/
        val orderNumber = "1"
        /*订单状态*/
        val orderStatus = "1"
        /*订单地址*/
        val address = "1"
        /*订单时间*/
        val time = "1"
        /*赋值*/
        if (context.getString(R.string.number_0) == type) {
            /*上门服务订单详情*/
            holder?.iv_order_type.setImageResource(R.drawable.appointment_icon)
            holder?.tv_service_time.setText(R.string.appointment_service_time)
            holder?.tv_service_address.setText(R.string.home_service_address)
        } else if (context.getString(R.string.number_1) == type) {
            /*救援服务订单详情*/
            holder?.iv_order_type.setImageResource(R.drawable.rescue_service_icon)
            holder?.tv_service_time.setText(R.string.rescue_service_time)
            holder?.tv_service_address.setText(R.string.rescue_service_address)
        }
        holder?.tv_time.text = if (TextUtils.isEmpty(time)) "" else time
        holder?.tv_address.text = if (TextUtils.isEmpty(address)) "" else address
        holder?.tv_order_number.text = if (TextUtils.isEmpty(orderNumber)) "" else orderNumber
        holder?.itemView.setOnClickListener {
            if (listener != null) {
                listener?.onClick(position, bean)
            }
        }
        holder?.itemView.setOnLongClickListener {
            if (listener != null) {
                listener?.onLongClick(position, bean)
            }
            true
        }
    }


    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_service_time = itemView.findViewById(R.id.tv_service_time) as TextView
        var tv_time = itemView.findViewById(R.id.tv_time) as TextView
        var tv_service_address = itemView.findViewById(R.id.tv_service_address) as TextView
        var tv_address = itemView.findViewById(R.id.tv_address) as TextView
        var tv_order_number = itemView.findViewById(R.id.tv_order_number) as TextView
        var tv_order_status = itemView.findViewById(R.id.tv_order_status) as TextView
        var iv_order_type = itemView.findViewById(R.id.iv_order_type) as ImageView
    }

    interface OnItemClickListener {
        fun onClick(position: Int, bean: JSONObject)
        fun onLongClick(position: Int, bean: JSONObject)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}