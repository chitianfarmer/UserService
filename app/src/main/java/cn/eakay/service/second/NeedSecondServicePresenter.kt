package cn.eakay.service.second

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import cn.eakay.service.R
import cn.eakay.service.address.SearchAddressActivity
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.messages.RefreshViewMessage
import cn.eakay.service.beans.response.LocationAddressBean
import cn.eakay.service.beans.response.TimeCheckBean
import cn.eakay.service.beans.response.WorkBean
import cn.eakay.service.network.ApiUtils
import cn.eakay.service.network.listener.ResultListener
import cn.eakay.service.network.listener.ResultObserver
import cn.eakay.service.network.transformer.SchedulerProvider
import cn.eakay.service.utils.StringUtils
import com.alibaba.fastjson.JSONObject
import com.shs.easywebviewsupport.utils.LogUtils
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus

/**
 * @packageName: UserService
 * @fileName: NeedSecondServicePresenter
 * @author: chitian
 * @date: 2019-11-27 09:24
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class NeedSecondServicePresenter : NeedSecondServiceController.Presenter {
    private var view: NeedSecondServiceController.View? = null
    private var timeList: MutableList<TimeCheckBean.TimeDayBeans> = ArrayList()
    private var defaultAddressBean: LocationAddressBean? = null
    private var checkedTimeBean: TimeCheckBean.TimeDayBeans.TimeTimeBeans? = null

    /**
     * request check time
     */
    @SuppressLint("CheckResult")
    override fun requestDayAndTime() {
        view!!.showLoadDialog()
        val params = JSONObject()
        params["num"] = Constants.NUMBER_SIX
        val body = StringUtils.createBody(params)
        val observable = ApiUtils.instance.service.getTimeList(body)
        observable.compose(SchedulerProvider.instance.applySchedulers())
            .subscribe(ResultObserver(object : ResultListener<TimeCheckBean> {

                override fun success(result: TimeCheckBean) {
                    view!!.hintLoadDialog()
                    timeList.clear()
                    val list = result.datas
                    if (!list.isNullOrEmpty()) {
                        timeList.addAll(list)
                    }
                    view!!.refreshList(timeList)
                    view!!.refreshRecyclerView(timeList.size)
                }

                override fun failed(error: Throwable?) {
                    view!!.hintLoadDialog()
                }
            }))
    }

    /**
     * get time list
     *
     * @return
     */
    override fun getTimeList(): MutableList<TimeCheckBean.TimeDayBeans>? {
        if (timeList == null) {
            timeList = ArrayList()
        }
        return timeList
    }

    /**
     * submit to service
     */
    override fun submit() {
        val activity = view!!.getBaseActivity()
        val orderId = view!!.getIntentOrderId()
        val address = view!!.getInputAddress()
        val inputTime = view!!.getInputTime()
        val reason = view!!.getInputReason()
        var dayTime: String? = ""
        if (checkedTimeBean == null && !TextUtils.isEmpty(inputTime)) {
            dayTime = inputTime
        }
        if (checkedTimeBean != null && TextUtils.isEmpty(inputTime)) {
            dayTime = checkedTimeBean!!.time
        }
        if (checkedTimeBean != null && !TextUtils.isEmpty(inputTime)) {
            dayTime = checkedTimeBean!!.time
        }
        if (TextUtils.isEmpty(dayTime)) {
            view!!.toast(R.string.please_enter_the_service_time)
            return
        }
        if (TextUtils.isEmpty(address)) {
            view!!.toast(R.string.please_enter_the_service_address)
            return
        }
        if (TextUtils.isEmpty(reason)) {
            view!!.toast(R.string.please_fill_in_the_reason_note)
            return
        }
        view?.showLoadDialog()
        val params = JSONObject()
        params["mainOrderId"] = orderId
        params["serviceTimeId"] = if (checkedTimeBean == null) "" else checkedTimeBean!!.id
        params["serviceTime"] = if (checkedTimeBean == null) "" else checkedTimeBean!!.time
        params["address"] =
            defaultAddressBean!!.address + defaultAddressBean!!.name+ ""
        params["xCoordinate"] = defaultAddressBean!!.lat.toString() + ""
        params["yCoordinate"] = defaultAddressBean!!.lng.toString()+ ""
        params["secondServiceBz"] = reason
        params["secondTimeDesc"] = if (TextUtils.isEmpty(inputTime)) "" else inputTime
        val body = StringUtils.createBody(params)
        ApiUtils.instance.service.addSecondService(body)
            .also {
                it.compose(SchedulerProvider.instance.applySchedulers())
                it.subscribe(ResultObserver(object :ResultListener<WorkBean>{

                    override fun success(result: WorkBean) {
                        view?.hintLoadDialog()
                        EventBus.getDefault().post(RefreshViewMessage(Constants.NUMBER_ZERO))
                        activity.finish()
                    }

                    override fun failed(error: Throwable?) {
                        view?.hintLoadDialog()
                    }
                }))
            }
    }

    /**
     * has address
     *
     * @param bean
     */
    override fun hasAddress(bean: LocationAddressBean?) {
        if (bean != null) {
            defaultAddressBean = bean
        }
    }

    /**
     * check address
     */
    override fun checkAddress() {
        val activity = view!!.getBaseActivity()
        val intent = Intent(activity, SearchAddressActivity::class.java)
        activity.startActivity(intent)
    }

    /**
     * check time bean
     *
     * @param bean
     */
    override fun checkedTimeBean(bean: TimeCheckBean.TimeDayBeans.TimeTimeBeans?) {
        if (bean != null) {
            checkedTimeBean = bean
        }
    }

    /**
     * 绑定view
     */
    override fun attachView(view: NeedSecondServiceController.View) {
        this.view = view
    }

    /**
     * 解绑View
     */
    override fun detachView() {
        view = null
    }
}