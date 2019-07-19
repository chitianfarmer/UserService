package cn.eakay.service.orders.house

import cn.eakay.service.base.BasePresenter
import cn.eakay.service.base.BaseView
import cn.eakay.service.beans.PictureMessage
import cn.eakay.service.beans.PictureOrderMessage
import java.util.ArrayList

/**
 * @packageName: UserService
 * @fileName: HouseOrderDetailContract
 * @author: chitian
 * @date: 2019-07-19 08:43
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class HouseOrderDetailContract {
    interface View : BaseView {
        /**
         * show order info
         *
         * @param orderStatus
         * @param orderNumber
         * @param orderType
         */
        fun showOrderInfo(orderStatus: String?, orderNumber: String?, orderType: String?)

        /**
         * show address info
         *
         * @param address
         * @param distance
         * @param money
         */
        fun showOrderAddressInfo(address: String?, distance: String?, money: String?)

        /**
         * show order create and service time
         *
         * @param orderCreateTime
         * @param orderServiceTime
         */
        fun showOrderDetail(orderCreateTime: String?, orderServiceTime: String?)

        /**
         * show car and user phone , remarks info
         *
         * @param carMode
         * @param carNumber
         * @param userPhone
         * @param userRemark
         */
        fun showCarAndUserInfo(carMode: String?, carNumber: String?, userPhone: String?, userRemark: String?)

        /**
         * show user add images
         *
         * @param imagePaths
         */
        fun showUserAddImages(imagePaths: ArrayList<PictureOrderMessage>)

        /**
         * show service images
         */
        fun showServiceImages(imagePaths: ArrayList<PictureOrderMessage>)

        /**
         * get intent order type
         *
         * @return
         */
        fun getIntentOrderType(): String?

        /**
         * get intent order id
         *
         * @return
         */
        fun getIntentOrderId(): String?

        /**
         * show nuaml menu
         */
        fun showNumalView()

        /**
         * show start service view
         */
        fun showOrderStartServiceView()

        /**
         * show order complete view
         */
        fun showOrderCompleteView()

        /**
         * show second service view
         */
        fun showSecondServiceView()

        /**
         * show take picture view
         */
        fun showTakePictureView()

        /**
         * hint take picture view
         */
        fun hintTakePictureView()

        /**
         * show service image view
         */
        fun showServiceImagesView()

        /**
         * hint service image view
         */
        fun hintServiceImagesView()

        /**
         * show second service
         */
        fun showSecondService(time: String?, address: String?)

        /**
         * show second service complete menu
         */
        fun showServiceCashCompleteView()

        /**
         * show second service money
         *
         * @param money
         */
        fun showServiceCashMoney(money: String?)

        /**
         * change start view text
         *
         * @param msg
         */
        fun changeStartText(msg: Any)

        /**
         * change start view text size
         *
         * @param size
         */
        fun changeStartTextSize(size: Float)

        /**
         * void show pay time
         *
         * @param time
         */
        fun showPayTime(time: String?)

        /**
         * show pay time view
         */
        fun showPayTimeView()

        /**
         * hint all menu view
         */
        fun hintAllMenuView()

        /**
         * show money view
         */
        fun showServiceListMoneyView()

        /**
         * show pay total and list
         *
         * @param serviceMoney
         * @param total
         * @param materialMoney
         */
        fun showPayList(serviceMoney: String?, materialMoney: String?, total: String?)

        /**
         * show service notes
         */
        fun showServiceNotesView()

        /**
         * show notes
         */
        fun showServiceNotes(notes: String?)

        /**
         * hint service notes
         */
        fun hintServiceNotesView()

        /**
         * 显示或者隐藏现金标示
         *
         * @param flag
         */
        fun showOrHintCashView(flag: Int)

    }

    interface Presenter : BasePresenter<View> {
        /**
         * request order info from service
         */
         fun requestOrderInfo()

        /**
         * work this order to add order
         */
         fun orderThis()

        /**
         * can not work this order
         */
         fun showCancelDialog()

        /**
         * open out map navigation
         */
         fun showNaviView()

        /**
         * call user
         */
         fun callUser()

        /**
         * start service
         */
         fun startService(type: Int)

        /**
         * start_rescue
         */
         fun startRescue()

        /**
         * to user home
         *
         * @param type
         */
         fun toUserHome(type: Int)

        /**
         * to edit service door fee
         */
         fun toEditFee()

        /**
         * service completion
         */
         fun orderComplete()

        /**
         * need secondary service
         */
         fun needSecondaryService()

        /**
         * take order service image
         */
         fun takeServiceImages()

        /**
         * get the service paths
         */
         fun getPathLists(): ArrayList<PictureOrderMessage>

        /**
         * upload images
         *
         * @param path
         */
         fun uploadPath(path: String)

        /**
         * show checked images
         *
         * @param message
         */
         fun onAblumResult(message: PictureMessage)

        /**
         * show cash dialog
         */
         fun showCashDialog()

        /**
         * on picture click
         *
         * @param position
         */
         fun onPictureClicked(position: Int, pathLists: ArrayList<PictureOrderMessage>)

        /**
         * see  service money detail
         */
         fun seeServiceMoneyDetail()
    }

}