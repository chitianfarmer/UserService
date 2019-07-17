package cn.eakay.service.tabs

import android.content.Context
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.utils.StringUtils
import com.alibaba.fastjson.JSONObject
import java.util.ArrayList

/**
 * @packageName: UserService
 * @fileName: TabPresenter
 * @author: chitian
 * @date: 2019-07-12 11:33
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class TabPresenter : TabContract.Presenter {

    private var view: TabContract.View? = null
    private var objectList: MutableList<JSONObject>? = ArrayList<JSONObject>()

    override fun getLists(): MutableList<JSONObject> {
        if (objectList == null) {
            objectList = ArrayList()
        }
        return objectList!!
    }

    override fun requestData(page: Int) {
//        val activity = view?.getBaseActivity() ?: return
        val type = view?.getIntentType()
        view?.showLoadDialog()
        val obj = JSONObject()
        /*待接待 <-3  待完成 <-5，6，7，12  待支付 <-2，8  已取消 <-4，9，10，11，13，14，15  已完成 <-1  订单类型(0:预约单，1：救援单 )*/
        if (type == Constants.NUMBER_ZERO) {
            obj["orderStatus"] = "3"
        } else if (type == Constants.NUMBER_ONE) {
            obj["orderStatus"] = "5,6,7,12"
        } else if (type == Constants.NUMBER_TWO) {
            //            object.put("orderStatus", "2,8");
            obj["orderStatus"] = "8"
        } else if (type == Constants.NUMBER_THREE) {
            obj["orderStatus"] = "4,9,10,11,13,14,15"
        } else if (type == Constants.NUMBER_FOUR) {
            obj["orderStatus"] = "1"
        }
        if (objectList!!.size > Constants.NUMBER_ZERO) {
            objectList!!.clear()
        }
        for (index in Constants.NUMBER_ZERO..Constants.NUMBER_TWELVE) {
            val jsonObject = JSONObject()
            objectList!!.add(jsonObject)
            view?.updateListView(objectList!!)
        }
//        val params = StringUtils.getListParams(page, Constants.PAGE_SIZE, obj)
//        NetAppActionImpl.getInstance().getOrderList(activity, params, object : CallBack<OrderListBean>() {
//            fun onSuccess(context: Context, response: OrderListBean) {
//                view?.hintLoadDialog()
//                val beans = response.getDatas()
//                if (beans != null && beans!!.size != Constants.NUMBER_ZERO) {
//                    view?.showListView()
//                    if (page == Constants.PAGE_COUNT) {
//                        objectList!!.clear()
//                        view?.stopRefresh()
//                    } else {
//                        view?.stopLoadMore()
//                    }
//                    objectList!!.addAll(beans!!)
//                    view?.updateListView(objectList)
//                    if (beans!!.size < Constants.PAGE_SIZE) {
//                        view?.setLoadMoreEnable(false)
//                        view?.setRefreshEnable(true)
//                    } else {
//                        view?.setLoadMoreEnable(true)
//                        view?.setRefreshEnable(true)
//                    }
//                } else {
//                    if (page == Constants.PAGE_COUNT) {
//                        objectList!!.clear()
//                        view?.stopRefresh()
//                        view?.showEmptyView()
//                    } else {
//                        view?.stopLoadMore()
//                        view?.toast(R.string.already_in_the_end)
//                    }
//                    view?.setLoadMoreEnable(false)
//                    view?.setRefreshEnable(true)
//                }
//            }
//
//            fun onAppError(code: String, msg: String) {
//                view?.hintLoadDialog()
//                if (page == Constants.PAGE_COUNT) {
//                    objectList!!.clear()
//                    view?.stopRefresh()
//                    view?.showEmptyView()
//                } else {
//                    view?.stopLoadMore()
//                    view?.toast(R.string.already_in_the_end)
//                }
//                view?.setLoadMoreEnable(false)
//                view?.setRefreshEnable(true)
//            }
//        }, OrderListBean::class.java)
    }

    override fun onItemClick(position: Int, bean: JSONObject) {
        view?.toast("条目被点击")

    }

    override fun onLongClick(position: Int, bean: JSONObject) {
        view?.toast("条目被长按")
    }

    override fun attchView(view: TabContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }
}