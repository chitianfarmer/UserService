package cn.eakay.service.beans

import java.io.Serializable

/**
 * @packageName: UserService
 * @fileName: OrderDetailBean
 * @author: chitian
 * @date: 2019-07-19 14:34
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class OrderDetailBean : BaseResponse() {
    /**
     * datas : {"addressId":1,"backMoney":0,"billInfoListVos":[{"describle":"","orderId":5,"payMode":"1","payMoney":360,"payStatus":"0","payType":"1"}],"cancelTime":"2019-05-14 15:11:41","carModelId":3,"carModelName":"东风ER30高配版","carNumber":"皖BZW901","courrentAddress":"芜湖市","customerId":0,"customerTelphone":"15655310218","distanceDestination":"120","doorMoney":360,"extraDisc":"","orderNumber":"SHDD02560201905141511","orderStatus":"9","orderType":"0","payAllMoney":360,"questionImg":"","secondAddress":"","secondServiceTime":null,"serviceCarId":"","serviceCode":"eakay.sh.201904261619","serviceImg":"","serviceMaterialMoney":0,"serviceMoney":0,"serviceTime":"2019-05-14 15:11:41","serviceWorkMoney":0,"slaveInfoListVos":[],"startTime":"2019-05-14 15:11:41","waitPayMoney":360,"xCoordinate":"199","yCoordinate":"166"}
     */

    private var datas: DatasBean? = null

    fun getDatas(): DatasBean? {
        return datas
    }

    fun setDatas(datas: DatasBean) {
        this.datas = datas
    }

    class DatasBean : Serializable {
        /**
         * addressId : 1
         * backMoney : 0
         * billInfoListVos : [{"describle":"","orderId":5,"payMode":"1","payMoney":360,"payStatus":"0","payType":"1"}]
         * cancelTime : 2019-05-14 15:11:41
         * carModelId : 3
         * carModelName : 东风ER30高配版
         * carNumber : 皖BZW901
         * courrentAddress : 芜湖市
         * customerId : 0
         * customerTelphone : 15655310218
         * distanceDestination : 120
         * doorMoney : 360
         * extraDisc :
         * orderNumber : SHDD02560201905141511
         * orderStatus : 9
         * orderType : 0
         * payAllMoney : 360
         * questionImg :
         * secondAddress :
         * secondServiceTime : null
         * serviceCarId :
         * serviceCode : eakay.sh.201904261619
         * serviceImg :
         * serviceMaterialMoney : 0
         * serviceMoney : 0
         * serviceTime : 2019-05-14 15:11:41
         * serviceWorkMoney : 0
         * slaveInfoListVos : []
         * startTime : 2019-05-14 15:11:41
         * waitPayMoney : 360
         * xCoordinate : 199
         * yCoordinate : 166
         * payTime : 2019-05-14 15:11:41
         * doorMoney:240.00
         * sysDoorMoney:60.00
         */

        var addressId: String? = null
        var backMoney: String? = null
        var cancelTime: String? = null
        var carModelId: String? = null
        var carModelName: String? = null
        var carNumber: String? = null
        var courrentAddress: String? = null
        var customerId: String? = null
        var customerTelphone: String? = null
        var distanceDestination: String? = null
        var doorMoney: String? = null
        var extraDisc: String? = null
        var orderNumber: String? = null
        var orderStatus: String? = null
        var orderType: String? = null
        var payAllMoney: String? = null
        var questionImg: String? = null
        var secondAddress: String? = null
        var secondServiceTime: String? = null
        var serviceCarId: String? = null
        var serviceCode: String? = null
        var serviceImg: String? = null
        var serviceMaterialMoney: String? = null
        var serviceMoney: String? = null
        var serviceTime: String? = null
        var serviceWorkMoney: String? = null
        var startTime: String? = null
        var waitPayMoney: String? = null
        var xCoordinate: String? = null
        var yCoordinate: String? = null
        var payTime: String? = null
        var createTime: String? = null
        var refuceReason: String? = null
        var sysDoorMoney: String? = null
        var billInfoListVos: List<BillInfoListVosBean>? = null
        var slaveInfoListVos: List<ServiceMoreBean>? = null

        fun getxCoordinate(): String? {
            return xCoordinate
        }

        fun setxCoordinate(xCoordinate: String) {
            this.xCoordinate = xCoordinate
        }

        fun getyCoordinate(): String? {
            return yCoordinate
        }

        fun setyCoordinate(yCoordinate: String) {
            this.yCoordinate = yCoordinate
        }

        class BillInfoListVosBean : Serializable {
            /**
             * describle :
             * orderId : 5
             * payMode : 1
             * payMoney : 360
             * payStatus : 0
             * payType : 1
             */

            var describle: String? = null
            var orderId: String? = null
            var payMode: String? = null
            var payMoney: String? = null
            var payStatus: String? = null
            var payTime: String? = null
            var payType: String? = null
        }

        class ServiceMoreBean : Serializable {
            /**
             * "address":"lalaalala",
             * "serviceCarId":1,
             * "serviceCarNumber":"皖B00001",
             * "serviceCarTelphone":"15375697970",
             * "serviceTime":"2019-01-01 00:00:00",
             * "xCoordinate":"666",
             * "yCoordinate":"999"
             */
            var address: String? = null
            var serviceCarId: String? = null
            var serviceCarNumber: String? = null
            var serviceCarTelphone: String? = null
            var serviceTime: String? = null
            private var xCoordinate: String? = null
            private var yCoordinate: String? = null

            fun getxCoordinate(): String? {
                return xCoordinate
            }

            fun setxCoordinate(xCoordinate: String) {
                this.xCoordinate = xCoordinate
            }

            fun getyCoordinate(): String? {
                return yCoordinate
            }

            fun setyCoordinate(yCoordinate: String) {
                this.yCoordinate = yCoordinate
            }
        }
    }
}