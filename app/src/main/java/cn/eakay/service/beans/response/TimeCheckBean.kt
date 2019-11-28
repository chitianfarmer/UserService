package cn.eakay.service.beans.response

import cn.eakay.service.beans.base.BaseResponse
import java.io.Serializable

/**
 * @packageName: UserService
 * @fileName: TimeCheckBean
 * @author: chitian
 * @date: 2019-11-27 09:15
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class TimeCheckBean : BaseResponse() {
    var datas: MutableList<TimeDayBeans>? = null

    class TimeDayBeans : Serializable {
        /**
         * subscribeInfoVoList : [{"flag":true,"id":231,"num":0,"pressDate":104,"time":"08:00~10:00"},{"flag":true,"id":232,"num":0,"pressDate":104,"time":"10:00~12:00"},{"flag":true,"id":233,"num":0,"pressDate":104,"time":"12:00~14:00"},{"flag":true,"id":234,"num":0,"pressDate":104,"time":"14:00~16:00"},{"flag":true,"id":235,"num":0,"pressDate":104,"time":"16:00~18:00"}]
         * titleDate : 5月16日
         * week : 星期四
         */
        var titleDate: String? = null
        var week: String? = null
        var subscribeInfoVoList: MutableList<TimeTimeBeans>? = null
        var isChecked:Boolean = false

        class TimeTimeBeans : Serializable {
            /**
             * flag : true
             * id : 231
             * num : 0
             * pressDate : 104
             * time : 08:00~10:00
             */
            var flag:Boolean = false
            var id: String? = null
            var num: String? = null
            var pressDate: String? = null
            var time: String? = null
            var isChecked:Boolean = false

        }
    }
}