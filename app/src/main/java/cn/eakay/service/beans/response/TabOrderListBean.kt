package cn.eakay.service.beans.response

import cn.eakay.service.beans.base.BaseResponse
import java.io.Serializable

/**
 * @packageName: UserService
 * @fileName: TabOrderListBean
 * @author: chitian
 * @date: 2019-07-18 08:11
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class TabOrderListBean : BaseResponse() {

    private var datas: List<OrderBean>? = null

    fun getDatas(): List<OrderBean>? {
        return datas
    }

    fun setDatas(datas: List<OrderBean>) {
        this.datas = datas
    }


    class OrderBean : Serializable {
        /**
         * courrentAddress : 芜湖市
         * id : 1
         * orderNumber : SHDD4144201904271535
         * orderStatus : 等待工作人员出发
         * orderType : 1
         * serviceCode : eakay.sh.201904261619
         * serviceTime : 2019-04-28 09:14:44
         */

        var courrentAddress: String? = null
        var id: String? = null
        var orderNumber: String? = null
        var orderStatus: String? = null
        var orderType: String? = null
        var serviceCode: String? = null
        var serviceTime: String? = null

    }

}
