package cn.eakay.service.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.eakay.service.R
import cn.eakay.service.beans.response.LocationAddressBean

/**
 * @packageName: UserService
 * @fileName: SearchAddressAdapter
 * @author: chitian
 * @date: 2019-11-27 08:18
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SearchAddressAdapter : RecyclerView.Adapter<SearchAddressAdapter.ViewHolder> {
    private var context: Context
    private var addressList: MutableList<LocationAddressBean>
    private var listener: OnSearchAddressClickListener? = null


    constructor(context: Context, addressList: MutableList<LocationAddressBean>) {
        this.context = context
        this.addressList = addressList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.item_address, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = addressList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val addressBean = addressList[position]
        holder.tvAddressTitle.text = addressBean.name
        holder.tvAddress.text = addressBean.address
        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onAddressClick(position, addressBean)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAddressTitle = itemView.findViewById<TextView>(R.id.tv_address_title)
        val tvAddress = itemView.findViewById<TextView>(R.id.tv_address)

    }

    interface OnSearchAddressClickListener {
        fun onAddressClick(position: Int, bean: LocationAddressBean)
    }

    fun setOnSearchAddressClickListener(listener: OnSearchAddressClickListener) {
        this.listener = listener
    }
}