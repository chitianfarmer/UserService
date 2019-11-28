package cn.eakay.service.second

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import cn.eakay.service.R
import cn.eakay.service.adapter.ServiceTimeDayAdapter
import cn.eakay.service.base.BaseFragment
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.messages.AddressMessage
import cn.eakay.service.beans.response.LocationAddressBean
import cn.eakay.service.beans.response.TimeCheckBean
import cn.eakay.service.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_search_address.recyclerView
import kotlinx.android.synthetic.main.fragment_second_service_need.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @packageName: UserService
 * @fileName: NeedSecondServiceFragment
 * @author: chitian
 * @date: 2019-11-27 09:10
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class NeedSecondServiceFragment : BaseFragment(), NeedSecondServiceController.View {
    private var presenter: NeedSecondServicePresenter? = null
    private var defaultDay: String? = ""
    private var defaultTime: String? = ""
    private var manager: GridLayoutManager? = null
    private var adapter: ServiceTimeDayAdapter? = null

    companion object {
        fun newInstance(orderId: String?): NeedSecondServiceFragment {
            val fragment = NeedSecondServiceFragment()
            val bundle = Bundle()
            bundle.putString(Constants.KEY_ORDER_ID, orderId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_second_service_need

    override fun bindView() {
        if (presenter == null) {
            presenter = NeedSecondServicePresenter()
        }
        presenter!!.attachView(this)
        initEvent()
    }

    override fun initData() {
        refreshRecyclerView(Constants.NUMBER_SEVEN)
        adapter = ServiceTimeDayAdapter(activity!!, presenter!!.getTimeList())
        recyclerView!!.adapter = adapter
        presenter!!.requestDayAndTime()
    }

    override fun setListener() {
        tv_address?.setOnClickListener(this)
        adapter!!.setOnServiceTimeClickListener(this)
    }

    /**
     * void refresh list
     *
     * @param timeCheckBeanList
     */
    override fun refreshList(timeCheckBeanList: List<TimeCheckBean.TimeDayBeans>?) {
        adapter!!.notifyDataSetChanged()
    }

    /**
     * refreshview type
     * @param size
     */
    override fun refreshRecyclerView(size: Int) {
        when (size) {
            Constants.NUMBER_ONE -> {
                manager = GridLayoutManager(activity, Constants.NUMBER_ONE)
            }
            Constants.NUMBER_TWO -> {
                manager = GridLayoutManager(activity, Constants.NUMBER_TWO)
            }
            Constants.NUMBER_THREE -> {
                manager = GridLayoutManager(activity, Constants.NUMBER_THREE)
            }
            Constants.NUMBER_FOUR -> {
                manager = GridLayoutManager(activity, Constants.NUMBER_FOUR)
            }
            Constants.NUMBER_FIVE -> {
                manager = GridLayoutManager(activity, Constants.NUMBER_FIVE)
            }
            Constants.NUMBER_SIX -> {
                manager = GridLayoutManager(activity, Constants.NUMBER_SIX)
            }
            else -> {
                manager = GridLayoutManager(activity, Constants.NUMBER_SEVEN)
            }
        }
        recyclerView!!.layoutManager = manager
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                activity,
                GridLayoutManager.HORIZONTAL
            )
        )
    }

    /**
     * get intent order Id
     *
     * @return
     */
    override fun getIntentOrderId(): String? = arguments!!.getString(Constants.KEY_ORDER_ID)

    /**
     * get input time
     *
     * @return
     */
    override fun getInputTime(): String? = edt_time.text.toString().trim()

    /**
     * get input reason
     *
     * @return
     */
    override fun getInputReason(): String? = edt_reason.text.toString().trim()

    /**
     * get input address
     *
     * @return
     */
    override fun getInputAddress(): String? = tv_address.text.toString().trim()

    /**
     * toast
     *
     * @param msg
     */
    override fun toast(msg: Any) {
        ToastUtils.showShort(msg)
    }

    /**
     * 显示弹窗
     */
    override fun showLoadDialog() {
        showProgress()
    }

    /**
     * 隐藏弹窗
     */
    override fun hintLoadDialog() {
        closeProgress()
    }

    /**
     * 获取依赖的activity
     *
     * @return
     */
    override fun getBaseActivity(): Activity = activity!!

    override fun onTimeClicked(
        dayBean: TimeCheckBean.TimeDayBeans?,
        timeBean: TimeCheckBean.TimeDayBeans.TimeTimeBeans?
    ) {
        defaultDay = dayBean?.titleDate
        defaultTime = timeBean?.time
        presenter?.checkedTimeBean(timeBean)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_address -> {
                presenter!!.checkAddress()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun checkedAddress(message: AddressMessage?) {
        if (message == null) {
            return
        }
        val code: Int = message.code
        if (code == Constants.NUMBER_ZERO) {
            val bean: LocationAddressBean = message.bean
            if (bean != null) {
                val address: String = bean.address!!
                val name: String = bean.name!!
                if (tv_address != null) {
                    tv_address.text = if (TextUtils.isEmpty(address)) "" else address + name
                }
            }
            presenter!!.hasAddress(bean)
        }
    }

    override fun onDestroy() {
        removeEvent()
        presenter?.detachView()
        super.onDestroy()
    }
}