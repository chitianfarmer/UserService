package cn.eakay.service.tabs

import android.annotation.SuppressLint
import android.content.Intent
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.TabOrderListBean
import cn.eakay.service.network.ApiUtils
import cn.eakay.service.network.listener.ResultListener
import cn.eakay.service.network.listener.ResultObserver
import cn.eakay.service.orders.house.HouseOrderDetailActivity
import cn.eakay.service.orders.rescue.RescueOrderDetailActivity
import cn.eakay.service.utils.ErrorManager
import cn.eakay.service.utils.StringUtils
import com.alibaba.fastjson.JSONObject
import com.shs.easywebviewsupport.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    private var objectList: MutableList<TabOrderListBean.OrderBean>? = ArrayList<TabOrderListBean.OrderBean>()

    override fun getLists(): MutableList<TabOrderListBean.OrderBean> {
        if (objectList == null) {
            objectList = ArrayList()
        }
        return objectList!!
    }

    @SuppressLint("CheckResult")
    override fun requestData(page: Int) {
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
        val params = StringUtils.getListParams(page, Constants.PAGE_SIZE, obj)
        val body = StringUtils.createBody(params)
        val orderList = ApiUtils.instance.service.requestOrderList(body)
        orderList.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(ResultObserver(object :
                ResultListener<TabOrderListBean> {
                override fun success(result: TabOrderListBean) {
                    when (result.getErrCode()) {
                        "0" -> {
                            val beans = result.getDatas()
                            view?.hintLoadDialog()
                            if (beans != null && beans.size != Constants.NUMBER_ZERO) {
                                view?.showListView()
                                if (page == Constants.PAGE_COUNT) {
                                    objectList!!.clear()
                                    view?.stopRefresh()
                                } else {
                                    view?.stopLoadMore()
                                }
                                objectList!!.addAll(beans)
                                view?.updateListView(objectList!!)
                            } else {
                                if (page == Constants.PAGE_COUNT) {
                                    objectList!!.clear()
                                    view?.stopRefresh()
                                    view?.showEmptyView()
                                } else {
                                    view?.stopLoadMore()
                                    view?.toast(R.string.already_in_the_end)
                                }
                            }
                        }
                        else -> {
                            view?.hintLoadDialog()
                            val errCode = result.getErrCode()
                            val errMsg = result.getErrMsg()
                            val resultError = ErrorManager.checkResultError(errCode, errMsg)
                            LogUtils.loge("请求列表信息：$resultError")
                            if (page == Constants.PAGE_COUNT) {
                                objectList!!.clear()
                                view?.stopRefresh()
                                view?.showEmptyView()
                            } else {
                                view?.stopLoadMore()
                                view?.toast(R.string.already_in_the_end)
                            }
                        }
                    }
                }

                override fun failed(error: Throwable?) {
                    val message = error?.message
                    LogUtils.loge("请求列表错误信息：$message")
                    view?.hintLoadDialog()
                    if (page == Constants.PAGE_COUNT) {
                        objectList!!.clear()
                        view?.stopRefresh()
                        view?.showEmptyView()
                        view?.toast("请求列表错误信息：$message")
                    } else {
                        view?.stopLoadMore()
                        view?.toast(R.string.already_in_the_end)
                    }
                }
            }
            ))

    }

    override fun onItemClick(position: Int, bean: TabOrderListBean.OrderBean) {
        val activity = view?.getBaseActivity()
        val orderType = bean.orderType
        val orderId = bean.id
        val intent = Intent()
        intent.setClass(
            activity,
            if (activity?.getString(R.string.number_0).equals(orderType)) {
                HouseOrderDetailActivity::class.java
            } else {
                RescueOrderDetailActivity::class.java
            }
        )
        intent.putExtra(Constants.KEY_ORDER_TYPE, orderType)
        intent.putExtra(Constants.KEY_ORDER_ID, orderId)
        activity?.startActivity(intent)
    }

    override fun onLongClick(position: Int, bean: TabOrderListBean.OrderBean) {
    }

    override fun attachView(view: TabContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }
}