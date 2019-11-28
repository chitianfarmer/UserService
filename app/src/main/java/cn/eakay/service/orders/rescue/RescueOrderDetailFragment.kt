package cn.eakay.service.orders.rescue

import android.app.Activity
import android.os.Bundle
import android.view.View
import cn.eakay.service.R
import cn.eakay.service.adapter.OrderImagesAdapter
import cn.eakay.service.base.BaseFragment
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.messages.PictureOrderMessage
import cn.eakay.service.beans.messages.RefreshViewMessage
import cn.eakay.service.orders.house.HouseOrderDetailFragment
import cn.eakay.service.utils.StringUtils
import cn.eakay.service.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_order_detail.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

/**
 * @packageName: UserService
 * @fileName: RescueOrderDetailFragment
 * @author: chitian
 * @date: 2019-07-20 10:21
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class RescueOrderDetailFragment : BaseFragment(), RescueOrderDetailContract.View, View.OnClickListener {
    private lateinit var adapterUsers: OrderImagesAdapter
    private lateinit var adapterService: OrderImagesAdapter
    private lateinit var presenter: RescueOrderDetailPresenter

    companion object {
        fun newInstance(orderType: String?, orderId: String?) = HouseOrderDetailFragment()
            .apply {
                arguments = Bundle().apply {
                    putString(Constants.KEY_ORDER_TYPE, orderType)
                    putString(Constants.KEY_ORDER_ID, orderId)
                }
            }
    }

    override fun getLayoutId(): Int = R.layout.fragment_order_detail

    override fun bindView() {
        presenter = RescueOrderDetailPresenter()
        presenter.attachView(this)
        initEvent()
    }

    override fun initData() {
        presenter.requestOrderInfo()
        adapterUsers = OrderImagesAdapter(activity!!)
        adapterService = OrderImagesAdapter(activity!!)
        gv_user_images.adapter = adapterUsers
        gv_service_images.adapter = adapterService

    }

    override fun setListener() {
        tv_look_over.setOnClickListener(this)
        tv_look_service.setOnClickListener(this)
        tv_take_photo.setOnClickListener(this)
        tv_see_details.setOnClickListener(this)
        tv_unable_to_service.setOnClickListener(this)
        tv_add_order.setOnClickListener(this)
        iv_navi_service.setOnClickListener(this)
        iv_call_service.setOnClickListener(this)
        tv_start_service.setOnClickListener(this)
        iv_call_service_complete.setOnClickListener(this)
        tv_service_completed.setOnClickListener(this)
        iv_call_service_need_second.setOnClickListener(this)
        tv_need_second_service.setOnClickListener(this)
        tv_service_no_need_service.setOnClickListener(this)
        tv_user_cash.setOnClickListener(this)

        adapterService.setOnGridViewItemClickListener(object :
            OrderImagesAdapter.OnGridViewItemClickListener {
            override fun onItemDeleteClick(path: PictureOrderMessage) {
                val pathLists = presenter.getPathLists()
                pathLists.remove(path)
                showServiceImages(pathLists)
            }

            override fun onItemClick(position: Int, path: ArrayList<PictureOrderMessage>) {
                presenter.onPictureClicked(position, path)
            }
        })
        adapterUsers.setOnGridViewItemClickListener(object : OrderImagesAdapter.OnGridViewItemClickListener {
            override fun onItemDeleteClick(bean: PictureOrderMessage) {

            }

            override fun onItemClick(position: Int, bean: ArrayList<PictureOrderMessage>) {
                presenter.onPictureClicked(position, bean)
            }
        })

    }

    override fun showOrderInfo(orderStatus: String?, orderNumber: String?, orderType: String?) {
        tv_order_status.text = StringUtils.getOrderStatus(getBaseActivity(), orderStatus, getString(R.string.number_0))
        tv_order_number.text =
            if (orderNumber.isNullOrEmpty()) {
                ""
            } else {
                orderNumber
            }
        tv_order_type.text =
            if (orderType.isNullOrEmpty()) {
                ""
            } else {
                if (getString(R.string.number_0) == orderType) {
                    getString(R.string.appointment_service)
                } else {
                    getString(R.string.emergency_road_rescue)
                }
            }
    }

    override fun showOrderAddressInfo(address: String?, distance: String?, money: String?) {
        tv_order_address.text =
            if (address.isNullOrEmpty()) {
                ""
            } else {
                address
            }
        tv_order_distance.text = String.format(
            getString(R.string.how_address),
            if (distance.isNullOrEmpty()) {
                Constants.FREE_BY_ZERO
            } else {
                distance
            }
        )
        tv_order_service_money.text = String.format(
            getString(R.string.how_money),
            if (money.isNullOrEmpty()) {
                Constants.FREE_BY_ZERO
            } else {
                money
            }
        )
    }

    override fun showOrderDetail(orderCreateTime: String?, orderServiceTime: String?) {
        tv_order_create_time.text =
            if (orderCreateTime.isNullOrEmpty()) {
                ""
            } else {
                orderCreateTime
            }
        tv_order_service_time.text =
            if (orderServiceTime.isNullOrEmpty()) {
                ""
            } else {
                orderServiceTime
            }
    }

    override fun showCarAndUserInfo(carMode: String?, carNumber: String?, userPhone: String?, userRemark: String?) {
        tv_order_car_mode.text =
            if (carMode.isNullOrEmpty()) {
                ""
            } else {
                carMode
            }
        tv_order_car_number.text =
            if (carNumber.isNullOrEmpty()) {
                ""
            } else {
                carNumber
            }
        tv_contact_number.text =
            if (userPhone.isNullOrEmpty()) {
                ""
            } else {
                userPhone
            }
        tv_user_remarks.text =
            if (userRemark.isNullOrEmpty()) {
                ""
            } else {
                userRemark
            }
    }

    override fun showUserAddImages(imagePaths: ArrayList<PictureOrderMessage>) {
        if (imagePaths.isNullOrEmpty()) {
            return
        }
        gv_user_images.visibility = View.VISIBLE
        adapterUsers.replaceAll(imagePaths)
    }

    override fun showServiceImages(imagePaths: ArrayList<PictureOrderMessage>) {
        if (imagePaths.isNullOrEmpty()) {
            return
        }
        gv_service_images.visibility = View.VISIBLE
        adapterService.replaceAll(imagePaths)
        if (imagePaths.size >= Constants.NUMBER_EIGHT) {
            hintTakePictureView()
        } else {
            showTakePictureView()
        }
    }
    override fun getIntentOrderId(): String? {
        return arguments?.getString(Constants.KEY_ORDER_ID)
    }

    override fun showNumalView() {
        ll_take_order_menu.visibility = View.VISIBLE
        ll_start_service_menu.visibility = View.GONE
        ll_service_completed.visibility = View.GONE
        ll_service_completed_need_second.visibility = View.GONE
        ll_service_second_complete.visibility = View.GONE
    }

    override fun showOrderStartServiceView() {
        ll_take_order_menu.visibility = View.GONE
        ll_start_service_menu.visibility = View.VISIBLE
        ll_service_completed.visibility = View.GONE
        ll_service_completed_need_second.visibility = View.GONE
        ll_service_second_complete.visibility = View.GONE
    }

    override fun showOrderCompleteView() {
        ll_take_order_menu.visibility = View.GONE
        ll_start_service_menu.visibility = View.GONE
        ll_service_completed.visibility = View.VISIBLE
        ll_service_completed_need_second.visibility = View.GONE
        ll_service_second_complete.visibility = View.GONE
    }

    override fun showTakePictureView() {
        ll_take_photo.visibility = View.VISIBLE
    }

    override fun hintTakePictureView() {
        ll_take_photo.visibility = View.GONE
    }

    override fun showServiceImagesView() {
        ll_service_images.visibility = View.VISIBLE
        gv_service_images.visibility = View.VISIBLE
    }

    override fun hintServiceImagesView() {
        ll_service_images.visibility = View.GONE
        gv_service_images.visibility = View.GONE
    }

    override fun showServiceCashCompleteView() {
        ll_take_order_menu.visibility = View.GONE
        ll_start_service_menu.visibility = View.GONE
        ll_service_completed.visibility = View.GONE
        ll_service_completed_need_second.visibility = View.GONE
        ll_service_second_complete.visibility = View.VISIBLE
    }

    override fun showServiceCashMoney(money: String?) {
        tv_amount_receivable.text =
            if (money.isNullOrEmpty()) {
                Constants.FREE_BY_ZERO
            } else {
                money
            }
    }

    override fun changeStartText(msg: Any) {
        when (msg) {
            is Int -> tv_start_service.text = getString(msg)
            is String -> tv_start_service.text = msg
        }
    }

    override fun changeStartTextSize(size: Float) {
        tv_start_service.textSize = size
    }

    override fun showPayTime(time: String?) {
        tv_pay_time.text =
            if (time.isNullOrEmpty()) {
                ""
            } else {
                time
            }
    }

    override fun showPayTimeView() {
        ll_user_pay_time.visibility = View.VISIBLE
    }

    override fun hintAllMenuView() {
        ll_menu.visibility = View.GONE
        ll_take_order_menu.visibility = View.GONE
        ll_start_service_menu.visibility = View.GONE
        ll_service_completed.visibility = View.GONE
        ll_service_completed_need_second.visibility = View.GONE
        ll_service_second_complete.visibility = View.GONE
    }

    override fun showServiceListMoneyView() {
        ll_service_money.visibility = View.VISIBLE
        ll_detail.visibility = View.VISIBLE
    }

    override fun showPayList(serviceMoney: String?, materialMoney: String?, total: String?) {
        tv_work_hours.text = String.format(
            getString(R.string.how_money),
            if (serviceMoney.isNullOrEmpty()) {
                Constants.FREE_BY_ZERO
            } else {
                serviceMoney
            }
        )
        tv_total_material_costs.text = String.format(
            getString(R.string.how_money),
            if (materialMoney.isNullOrEmpty()) {
                Constants.FREE_BY_ZERO
            } else {
                materialMoney
            }
        )
        tv_service_total_money.text = String.format(
            getString(R.string.how_money),
            if (total.isNullOrEmpty()) {
                Constants.FREE_BY_ZERO
            } else {
                total
            }
        )
    }

    override fun showServiceNotesView() {
        tv_service_note.visibility = View.VISIBLE
        tv_service_note_tips.visibility = View.VISIBLE
    }

    override fun showServiceNotes(notes: String?) {
        tv_service_note.text =
            if (notes.isNullOrEmpty()) {
                ""
            } else {
                notes
            }
    }

    override fun hintServiceNotesView() {
        tv_service_note.visibility = View.GONE
        tv_service_note_tips.visibility = View.GONE
    }

    override fun showOrHintCashView(flag: Int) {
        tv_cash.visibility = flag
    }

    override fun toast(msg: Any) {
        ToastUtils.showShort(msg)
    }

    override fun showLoadDialog() {
        showProgress()
    }

    override fun hintLoadDialog() {
        closeProgress()
    }

    override fun getBaseActivity(): Activity = activity!!

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_look_over ->
                if (gv_user_images.visibility == View.GONE) {
                    gv_user_images.visibility = View.VISIBLE
                } else {
                    gv_user_images.visibility = View.GONE
                }
            R.id.tv_look_service ->
                if (gv_service_images.visibility == View.GONE) {
                    gv_service_images.visibility = View.VISIBLE
                } else {
                    gv_service_images.visibility = View.GONE
                }
            R.id.tv_take_photo ->
                presenter.takeServiceImages()
            R.id.tv_see_details ->
                presenter.seeServiceMoneyDetail()
            R.id.tv_unable_to_service ->
                presenter.showCancelDialog()
            R.id.tv_add_order ->
                presenter.orderThis()
            R.id.iv_navi_service ->
                presenter.showNaviView()
            R.id.iv_call_service ->
                presenter.callUser()
            R.id.tv_start_service -> {
                val content = tv_start_service.text.toString().trim()
                when {
                    getString(R.string.departure_rescue) == content ->
                        /*前往救援*/
                        presenter.toRescueAddress()
                    getString(R.string.start_rescue) == content ->
                        /*开始救援*/
                        presenter.startRescue()
                    getString(R.string.on_site_service_fee_is_sent_to_the_user).endsWith(content) ->
                        /*输入上门服务费*/
                        presenter.toEditFee()
                }
            }
            R.id.iv_call_service_complete ->
                presenter.callUser()
            R.id.tv_service_completed ->
                presenter.orderComplete()
            R.id.iv_call_service_need_second ->
                presenter.callUser()
            R.id.tv_need_second_service ->
                presenter.orderComplete()
            R.id.tv_service_no_need_service ->
                presenter.orderComplete()
            R.id.tv_user_cash ->
                presenter.showCashDialog()
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOrderStatus(message: RefreshViewMessage?) {
        if (message == null) {
            return
        }
        val code: Int = message.code
        if (code == Constants.NUMBER_ZERO) {
            presenter.getPathLists().clear()
            presenter.requestOrderInfo()
        }
    }
    override fun onDestroy() {
        removeEvent()
        presenter?.detachView()
        super.onDestroy()
    }
}