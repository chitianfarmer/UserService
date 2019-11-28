package cn.eakay.service.address

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.view.View
import android.widget.ImageView
import android.widget.ZoomControls
import androidx.recyclerview.widget.GridLayoutManager
import cn.eakay.service.R
import cn.eakay.service.adapter.SearchAddressAdapter
import cn.eakay.service.base.BaseFragment
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.messages.FinishViewMessage
import cn.eakay.service.beans.messages.LocationMessage
import cn.eakay.service.beans.response.LocationAddressBean
import cn.eakay.service.utils.ToastUtils
import cn.eakay.service.widget.CommonDialog
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatusUpdate
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import kotlinx.android.synthetic.main.fragment_search_address.*
import kotlinx.android.synthetic.main.layout_search_title.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @packageName: UserService
 * @fileName: SuccessAddressFragment
 * @author: chitian
 * @date: 2019-11-26 16:03
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SearchAddressFragment : BaseFragment(), SearchAddressController.View {
    private var presenter: SearchAddressPresenter? = null
    private var viewMap: BaiduMap? = null
    private var adapter: SearchAddressAdapter? = null

    companion object {
        fun newInstance(): SearchAddressFragment {
            return SearchAddressFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_address
    }

    override fun bindView() {
        if (presenter == null) {
            presenter = SearchAddressPresenter()
        }
        presenter!!.attachView(this)
        initEvent()
    }

    override fun initData() {
        viewMap = mapView!!.map
        viewMap!!.mapType = BaiduMap.MAP_TYPE_NORMAL
        val childAt = mapView?.getChildAt(1)
        if (childAt is ImageView || childAt is ZoomControls) {
            childAt.visibility = View.INVISIBLE
        }
        mapView!!.showScaleControl(false)
        mapView!!.showZoomControls(false)
        val update = MapStatusUpdateFactory.zoomTo(15.0f)
        viewMap!!.setMapStatus(update)
        viewMap!!.isMyLocationEnabled = true
        recyclerView!!.layoutManager = GridLayoutManager(activity!!, Constants.NUMBER_ONE)
        adapter = SearchAddressAdapter(activity!!, presenter!!.getPoiAddressList())
        recyclerView!!.adapter = adapter
        presenter!!.startLocation()

    }

    override fun setListener() {
        iv_re_location.setOnClickListener(this)
        tv_search.setOnClickListener(this)
        rl_search.setOnClickListener(this)
        viewMap!!.setOnMapTouchListener(presenter)
        viewMap!!.setOnMapStatusChangeListener(presenter)
        adapter!!.setOnSearchAddressClickListener(this)
    }

    /**
     * 刷新view
     */
    override fun updateListView() {
        adapter!!.notifyDataSetChanged()
    }

    /**
     * 显示我的位置
     */
    override fun showMyLocation(location: MyLocationData) {
        viewMap!!.setMyLocationData(location)
    }

    /**
     *
     * 移动地图视角
     */
    override fun animateMapStatus(status: MapStatusUpdate) {
        viewMap!!.animateMapStatus(status)
    }
    /**
     * 请打开定位权限
     */
    override fun pleaseEnableYourLocationService() {
        val builder = CommonDialog.Builder(activity!!)
        builder.setMessage(R.string.open_location)
            .setTitle(R.string.tips)
            .setPositiveButton(R.string.to_open)
            .setNegativeButton(R.string.dialog_negative_button_text)
            .setOnDialogClickListener(object : CommonDialog.OnDialogClickListener {
                override fun onConfirmClick(dialog: Dialog?, which: Int) {
                    dialog?.dismiss()
                    jump2PermissionSettings()
                }

                override fun onCancelClick(dialog: Dialog?, which: Int) {
                    dialog?.dismiss()
                }
            })
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    /**
     * 请求定位权限
     */
    override fun requestLocationPermission() {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        requestPermission(locationPermissions)
    }

    /**
     * 显示或者隐藏marker
     */
    override fun hintOrShowMarker(flag: Int) {
        iv_marker!!.visibility = flag
    }

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
    override fun getBaseActivity(): Activity {
        return activity!!
    }

    override fun onAddressClick(position: Int, bean: LocationAddressBean) {
        presenter!!.onPoiAddressClick(position, bean)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search, R.id.rl_search -> {
                presenter!!.showSearCheView()
            }
            R.id.iv_re_location -> {
                presenter!!.startLocation()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onDestroy() {
        removeEvent()
        mapView?.onDestroy()
        presenter?.detachView()
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOrderFinish(message: FinishViewMessage) {
        if (message == null) {
            return
        }
        val code = message.code
        if (code == Constants.NUMBER_ONE) {
            activity!!.finish()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationChange(message: LocationMessage) {
        presenter?.onLocationChanged(message)
    }
}