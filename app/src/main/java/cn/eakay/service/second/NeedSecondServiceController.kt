package cn.eakay.service.second

import android.view.View
import cn.eakay.service.adapter.ServiceTimeDayAdapter
import cn.eakay.service.base.BasePresenter
import cn.eakay.service.base.BaseView
import cn.eakay.service.beans.response.LocationAddressBean
import cn.eakay.service.beans.response.TimeCheckBean

/**
 * @packageName: UserService
 * @fileName: NeedSecondServiceController
 * @author: chitian
 * @date: 2019-11-27 09:11
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class NeedSecondServiceController {
    interface View : BaseView, ServiceTimeDayAdapter.OnServiceTimeClickListener,android.view.View.OnClickListener {
        /**
         * void refresh list
         *
         * @param timeCheckBeanList
         */
        fun refreshList(timeCheckBeanList: List<TimeCheckBean.TimeDayBeans>?)

        /**
         * refreshview type
         * @param type
         */
        fun refreshRecyclerView(type: Int)

        /**
         * get intent order Id
         *
         * @return
         */
        fun getIntentOrderId(): String?
        /**
         * get input time
         *
         * @return
         */
        fun getInputTime(): String?

        /**
         * get input reason
         *
         * @return
         */
        fun getInputReason(): String?

        /**
         * get input address
         *
         * @return
         */
        fun getInputAddress(): String?

    }
    interface Presenter :BasePresenter<View>{
        /**
         * request check time
         */
        fun requestDayAndTime()

        /**
         * get time list
         *
         * @return
         */
        fun getTimeList(): MutableList<TimeCheckBean.TimeDayBeans>?

        /**
         * submit to service
         */
        fun submit()

        /**
         * has address
         *
         * @param bean
         */
        fun hasAddress(bean: LocationAddressBean?)

        /**
         * check address
         */
        fun checkAddress()

        /**
         * check time bean
         *
         * @param bean
         */
        fun checkedTimeBean(bean: TimeCheckBean.TimeDayBeans.TimeTimeBeans?)
    }
}