package cn.eakay.service.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.PictureOrderMessage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @packageName: UserService
 * @fileName: OrderImagesAdapter
 * @author: chitian
 * @date: 2019-07-19 16:05
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class OrderImagesAdapter : BaseAdapter {
    private var listener: OnGridViewItemClickListener? = null
    private var context: Context? = null
    private var list: ArrayList<PictureOrderMessage>? = null

    constructor(context: Context) {
        this.context = context
        if (list == null) {
            list = ArrayList()
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder? = null
        var view: View? = null
        if (view == null) {
            view = View.inflate(context, R.layout.item_order_images, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder?
        }
        val bean = list!![position]
        val type = bean.getPicType()
        val localPath = bean.getLocalPath()
        val remotePath = bean.getRemotePath()
        //显示图片的配置
        Glide.with(context!!)
            .asBitmap()
            .load(
                if (type == Constants.NUMBER_ZERO) {
                    localPath
                } else {
                    if (localPath.isNullOrEmpty()) {
                        remotePath
                    } else {
                        localPath
                    }
                }
            )
            .thumbnail(0.5f)
            .placeholder(R.drawable.default_placeholder_img)
            .error(R.drawable.default_placeholder_img)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(holder?.ivOrderImageView!!)
        holder?.deleteView?.visibility = if (type == Constants.NUMBER_ZERO) View.VISIBLE else View.GONE
        holder?.deleteView?.setOnClickListener(View.OnClickListener {
            if (listener != null) {
                listener?.onItemDeleteClick(bean)
            }
        })
        holder?.ivOrderImageView?.setOnClickListener(View.OnClickListener {
            if (listener != null) {
                listener?.onItemClick(position, list!!)
            }
        })
        return view!!
    }

    override fun getItem(position: Int): Any = list!![position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list!!.size

    class ViewHolder {
        var ivOrderImageView: ImageView? = null
        var deleteView: ImageView? = null

        constructor(view: View) {
            ivOrderImageView = view.findViewById<ImageView>(R.id.iv_order_images)
            deleteView = view.findViewById<ImageView>(R.id.iv_delete)
        }
    }

    fun replaceAll(elem: List<PictureOrderMessage>) {
        list?.clear()
        list?.addAll(elem)
        notifyDataSetChanged()
    }

    interface OnGridViewItemClickListener {

        fun onItemDeleteClick(bean: PictureOrderMessage)

        fun onItemClick(position: Int, bean: ArrayList<PictureOrderMessage>)
    }

    fun setOnGridViewItemClickListener(listener: OnGridViewItemClickListener) {
        this.listener = listener
    }
}