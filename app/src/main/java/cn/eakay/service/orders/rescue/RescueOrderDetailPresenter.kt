package cn.eakay.service.orders.rescue

import android.view.View
import cn.eakay.service.R
import cn.eakay.service.beans.OrderDetailBean
import cn.eakay.service.beans.PictureMessage
import cn.eakay.service.beans.PictureOrderMessage
import cn.eakay.service.network.ApiUtils
import cn.eakay.service.network.listener.ResultListener
import cn.eakay.service.network.listener.ResultObserver
import cn.eakay.service.utils.StringUtils
import com.alibaba.fastjson.JSONObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

/**
 * @packageName: UserService
 * @fileName: RescueOrderDetailPresenter
 * @author: chitian
 * @date: 2019-07-20 10:25
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class RescueOrderDetailPresenter : RescueOrderDetailContract.Presenter {
    private var view: RescueOrderDetailContract.View? = null
    private var pathList: ArrayList<PictureOrderMessage>? = ArrayList()
    private var bean: OrderDetailBean.DatasBean? = null
    private var infoListVos: List<OrderDetailBean.DatasBean.ServiceMoreBean>? = null
    private var listVos: List<OrderDetailBean.DatasBean.BillInfoListVosBean>? = null
    /**
     * has image upload
     */
    var isNowUploadPic = false

    override fun requestOrderInfo() {
        val activity = view?.getBaseActivity()
        view?.showLoadDialog()
        val params = JSONObject()
        params["id"] = view?.getIntentOrderId()
        val body = StringUtils.createBody(params)
        val observable = ApiUtils.instance.service.getOrderDetailInfo(body)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(ResultObserver(object :
                ResultListener<OrderDetailBean> {
                override fun success(result: OrderDetailBean) {
                    view?.hintLoadDialog()
                    val bean = result.getDatas()
                    /*订单状态*/
                    val orderStatus = bean?.orderStatus
                    /*订单类型*/
                    val orderType = bean?.orderType
                    /*订单编号*/
                    val orderNumber = bean?.orderNumber
                    /*首次服务地址*/
                    val address = bean?.courrentAddress
                    /*二次服务地址*/
                    val secondAddress = bean?.secondAddress
                    /*首次服务时间*/
                    val serviceTime = bean?.serviceTime
                    /*二次服务时间*/
                    val secondServiceTime = bean?.secondServiceTime
                    /*车牌号*/
                    val carNumber = bean?.carNumber
                    /*上门里程*/
                    val destination = bean?.distanceDestination
                    /*下单时间*/
                    val createTime = bean?.createTime
                    /*服务开始时间*/
                    val startTime = bean?.startTime
                    /*服务取消时间*/
                    val cancelTime = bean?.cancelTime
                    /*客户服务需求*/
                    val extraDisc = bean?.extraDisc
                    /*车型id*/
                    val carModelId = bean?.carModelId
                    /*客户补充图片*/
                    val questionImg = bean?.questionImg
                    /*服务补充图片*/
                    val serviceImg = bean?.serviceImg
                    /*服务备注*/
                    var serviceNotes = bean?.refuceReason
                    /*等待支付的钱款*/
                    val payMoney = bean?.waitPayMoney
                    /*所有钱款*/
                    val payAllMoney = bean?.payAllMoney
                    /*返钱*/
                    val backMoney = bean?.backMoney
                    /*用户手机号*/
                    val telphone = bean?.customerTelphone
                    /*车辆类型*/
                    val carModelName = bean?.carModelName
                    /*服务费总金额*/
                    val totalMoney = bean?.serviceMoney
                    /*材料费*/
                    val materialMoney = bean?.serviceMaterialMoney
                    /*工时费*/
                    val hourMoney = bean?.serviceWorkMoney
                    /*上门服务费*/
                    val doorMoney = bean?.doorMoney
                    /*系统预告上门服务费*/
                    val sysDoorMoney = bean?.sysDoorMoney
                    /*用户支付时间*/
                    var payTime = bean?.payTime
                    /* 账单列表数据*/
                    listVos = bean?.billInfoListVos
                    /*二次服务地址*/
                    infoListVos = bean?.slaveInfoListVos
                    /*账单列表的备注*/
                    if (serviceNotes.isNullOrEmpty()) {
                        if (!listVos!!.isNullOrEmpty()) {
                            val vosBean = listVos!![0]
                            serviceNotes = vosBean.describle
                            val module = vosBean.payMode
                            payTime = vosBean.payTime
                            view?.showOrHintCashView(
                                when {
                                    module.isNullOrEmpty() -> View.GONE
                                    activity?.getString(
                                        R.string.number_3
                                    ) == module -> View.VISIBLE
                                    else -> View.GONE
                                }
                            )
                        }
                    }
                    /*用户图片*/
                    val userPaths = ArrayList<PictureOrderMessage>()
                    if (!questionImg.isNullOrEmpty()) {
                        userPaths.addAll(StringUtils.splitStringToList(questionImg))
                    }
                    /*服务图片*/
                    val servicePaths = ArrayList<PictureOrderMessage>()
                    if (!serviceImg.isNullOrEmpty()) {
                        servicePaths.addAll(StringUtils.splitStringToList(serviceImg))
                        pathList?.addAll(servicePaths)
                    }
                    view?.showUserAddImages(userPaths)
                    view?.showServiceImages(servicePaths)
                    view?.showCarAndUserInfo(carModelName, carNumber, telphone, extraDisc)
                    view?.showOrderAddressInfo(address, destination, sysDoorMoney)
                    view?.showOrderInfo(orderStatus, orderNumber, orderType)
                    view?.showOrderDetail(
                        createTime,
                        activity?.getString(R.string.start_immediately)
                    )
                    view?.showServiceNotes(serviceNotes)
                    view?.showServiceCashMoney(payMoney)
                    view?.showPayList(hourMoney, materialMoney, totalMoney)
                    view?.showPayTime(payTime)
                    changViewStatus(orderStatus)
                }

                override fun failed(error: Throwable?) {
                    view?.hintLoadDialog()
                }
            }
            ))
    }

    override fun orderThis() {

    }

    override fun showCancelDialog() {

    }

    override fun showNaviView() {

    }

    override fun callUser() {

    }

    override fun startRescue() {

    }

    override fun toRescueAddress() {

    }

    override fun toEditFee() {

    }

    override fun orderComplete() {

    }

    override fun takeServiceImages() {

    }

    override fun getPathLists(): ArrayList<PictureOrderMessage> {
        if (pathList == null) {
            pathList = ArrayList()
        }
        return pathList!!
    }

    override fun uploadPath(path: String?) {

    }

    override fun onAblumResult(message: PictureMessage) {

    }

    override fun showCashDialog() {

    }

    override fun onPictureClicked(position: Int, pathLists: ArrayList<PictureOrderMessage>) {

    }

    override fun seeServiceMoneyDetail() {

    }

    override fun attachView(view: RescueOrderDetailContract.View) {
        this.view = view

    }

    override fun detachView() {
        view = null
    }

    /**
     * 根据状态 判断现实隐藏不同的菜单
     *
     * @param status
     */
    private fun changViewStatus(status: String?) {
        val context = view?.getBaseActivity()
        if (context == null || bean == null) {
            view?.showNumalView()
            view?.hintServiceImagesView()
            view?.hintServiceNotesView()
            view?.hintTakePictureView()
            return
        }
        if (status.isNullOrEmpty()) {
            view?.showNumalView()
            view?.hintServiceImagesView()
            view?.hintServiceNotesView()
            view?.hintTakePictureView()
            return
        }
        var reason = bean?.refuceReason
        var payTime = bean?.payTime
        if (reason.isNullOrEmpty()) {
            if (!listVos!!.isNullOrEmpty()) {
                val vosBean = listVos!![0]
                reason = vosBean.describle
                payTime = vosBean.payTime
            }
        }
        if (context.getString(R.string.number_0) == status) {
            /* 订单初始状态*/
            view?.showNumalView()
            view?.hintServiceImagesView()
            view?.hintServiceNotesView()
            view?.hintTakePictureView()
        } else if (context.getString(R.string.number_1) == status) {
            /* 服务完支付完结束*/
            view?.hintAllMenuView()
            view?.showServiceImagesView()
            if (reason.isNullOrEmpty()) {
                view?.hintServiceNotesView()
            } else {
                view?.showServiceNotesView()
            }
            view?.showServiceListMoneyView()
            if (!payTime.isNullOrEmpty()) {
                view?.showPayTimeView()
            }
            view?.hintTakePictureView()
        } else if (context.getString(R.string.number_2) == status) {
            /* 等待用户支付上门费用*/
            view?.hintTakePictureView()
            view?.showServiceImagesView()
            if (reason.isNullOrEmpty()) {
                view?.hintServiceNotesView()
            } else {
                view?.showServiceNotesView()
            }
            view?.showServiceListMoneyView()
            view?.showServiceCashCompleteView()
        } else if (context.getString(R.string.number_3) == status) {
            /* 等待系统安排车辆*/
            view?.showNumalView()
            view?.hintTakePictureView()
            view?.hintServiceNotesView()
            view?.hintServiceImagesView()
        } else if (context.getString(R.string.number_4) == status) {
            /* 车辆繁忙系统取消退款中*/
            view?.hintAllMenuView()
            if (reason.isNullOrEmpty()) {
                view?.hintServiceNotesView()
            } else {
                view?.showServiceNotesView()
            }
            view?.hintTakePictureView()
            view?.hintServiceImagesView()
        } else if (context.getString(R.string.number_5) == status) {
            /* 等待工作人员出发*/
            view?.hintServiceImagesView()
            view?.hintTakePictureView()
            view?.showOrderStartServiceView()
            view?.hintServiceNotesView()
            view?.changeStartText(R.string.departure_rescue)
            view?.changeStartTextSize(16f)
        } else if (context.getString(R.string.number_6) == status) {
            /* 正在赶往目的地*/
            view?.hintServiceImagesView()
            view?.hintTakePictureView()
            view?.hintServiceNotesView()
            view?.showOrderStartServiceView()
            view?.changeStartText(R.string.start_rescue)
            view?.changeStartTextSize(16f)
        } else if (context.getString(R.string.number_7) == status) {
            /* 服务中*/
            if (reason.isNullOrEmpty()) {
                view?.hintServiceNotesView()
            } else {
                view?.showServiceNotesView()
            }
            view?.showOrderCompleteView()
            view?.showServiceImagesView()
            view?.showTakePictureView()
        } else if (context.getString(R.string.number_8) == status) {
            /* 服务完毕等待支付费用*/
            view?.hintTakePictureView()
            view?.showServiceImagesView()
            if (reason.isNullOrEmpty()) {
                view?.hintServiceNotesView()
            } else {
                view?.showServiceNotesView()
            }
            view?.showServiceListMoneyView()
            view?.showServiceCashCompleteView()
        } else if (context.getString(R.string.number_9) == status) {
            /* 超时支付上门费用取消结束*/
            view?.hintServiceImagesView()
            view?.hintServiceNotesView()
            view?.hintAllMenuView()
            view?.hintTakePictureView()
        } else if (context.getString(R.string.number_10) == status) {
            /* 客户主动取消退款中*/
            view?.hintAllMenuView()
            view?.hintServiceImagesView()
            view?.hintTakePictureView()
            view?.hintServiceNotesView()
        } else if (context.getString(R.string.number_11) == status) {
            /* 客户主动取消结束*/
            view?.hintServiceImagesView()
            view?.hintServiceNotesView()
            view?.hintAllMenuView()
            view?.hintTakePictureView()
        } else if (context.getString(R.string.number_12) == status) {
            /* 等待二次上门服务*/
            view?.hintServiceImagesView()
            view?.hintServiceNotesView()
            view?.showOrderStartServiceView()
            view?.changeStartText(R.string.start_rescue)
            view?.changeStartTextSize(16f)
            view?.hintTakePictureView()
        } else if (context.getString(R.string.number_13) == status) {
            /* 车辆繁忙系统取消退款结束*/
            view?.hintAllMenuView()
            if (reason.isNullOrEmpty()) {
                view?.hintServiceNotesView()
            } else {
                view?.showServiceNotesView()
            }
            view?.hintServiceImagesView()
            view?.hintTakePictureView()
        } else if (context.getString(R.string.number_14) == status) {
            /* 救援核算上门服务费*/
            view?.hintTakePictureView()
            view?.hintServiceNotesView()
            view?.hintServiceImagesView()
            view?.showOrderStartServiceView()
            view?.changeStartText(R.string.on_site_service_fee_is_sent_to_the_user)
            view?.changeStartTextSize(14f)
        } else if (context.getString(R.string.number_15) == status) {
            /* 客户取消后，等待支付救援上门服务费*/
            view?.hintServiceImagesView()
            view?.hintServiceNotesView()
            view?.hintAllMenuView()
            view?.hintTakePictureView()
        }
    }
}