package cn.eakay.service.address

import android.Manifest
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.messages.AddressMessage
import cn.eakay.service.beans.messages.LocationMessage
import cn.eakay.service.beans.response.LocationAddressBean
import cn.eakay.service.network.transformer.SchedulerProvider
import cn.eakay.service.utils.BdLocationHelper
import cn.eakay.service.utils.PermissionUtils.hasPermissions
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * @packageName: UserService
 * @fileName: SearchAddressPresenter
 * @author: chitian
 * @date: 2019-11-26 16:36
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SearchAddressPresenter : SearchAddressController.Presenter {
    /**
     * 绑定的view
     */
    private var view: SearchAddressController.View? = null
    /**
     * poiSearch对象
     */
    private var searchPoi: PoiSearch? = null
    /**
     * 是否第一次定位
     */
    private var isFirstLoc: Boolean = true
    /**
     * 按钮：回到原地
     */
    private var isTouch: Boolean = true
    /**
     * 搜索的字段
     */
    private val searchText: String = "小区"
    /**
     * 默认定位位置
     */
    private var defaultAddress: LocationAddressBean? = null
    /**
     * 初始化数据
     */
    private var poiAddressList: MutableList<LocationAddressBean>? = ArrayList()
    /**
     * 默认搜索的页面
     */
    private var index: Int = Constants.NUMBER_ZERO

    /**
     * 开启定位
     */
    override fun startLocation() {
        val activity = view!!.getBaseActivity()
        if (!BdLocationHelper.getInstance().isLocationEnabled(activity)) {
            view!!.pleaseEnableYourLocationService()
            return
        }
        if (!hasPermissions(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            view!!.requestLocationPermission()
            return
        }
        isTouch = true
        isFirstLoc = true
        BdLocationHelper.getInstance().startLocation()
    }

    /**
     * 结束定位
     */
    override fun stopLocation() {
        BdLocationHelper.getInstance().stopLocation()
    }

    /**
     * 显示搜索地址的view
     */
    override fun showSearCheView() {

    }

    /**
     * 刷新搜索
     */
    override fun refreshSearch() {
        poiAddressList!!.clear()
        index = Constants.NUMBER_ZERO
        val lat = defaultAddress?.lat
        val lng = defaultAddress?.lng
        val latLng = LatLng(lat!!, lng!!)
        searchPoiAddressInfo(latLng)
    }

    /**
     * 加载更多搜索
     */
    override fun loadMoreSearch() {
        index++
        val lat = defaultAddress?.lat
        val lng = defaultAddress?.lng
        val latLng = LatLng(lat!!, lng!!)
        searchPoiAddressInfo(latLng)
    }

    /**
     * 搜索地址
     */
    override fun searchPoiInfo(bean: LocationAddressBean) {
        val latLng = LatLng(bean.lat, bean.lng)
        searchPoiAddressInfo(latLng)
    }

    /**
     * 获取搜索到的地址列表
     */
    override fun getPoiAddressList(): MutableList<LocationAddressBean> {
        if (poiAddressList.isNullOrEmpty()) {
            poiAddressList = ArrayList()
        }
        return poiAddressList!!
    }

    /**
     * 地址条目被点击
     */
    override fun onPoiAddressClick(position: Int, bean: LocationAddressBean) {
        val activity = view!!.getBaseActivity()
        EventBus.getDefault().post(
            AddressMessage(
                Constants.NUMBER_ZERO,
                bean
            )
        )
        activity.finish()
    }

    override fun onLocationChanged(message: LocationMessage) {
        when(message.code){
            Constants.NUMBER_ZERO->{
                view!!.hintOrShowMarker(View.VISIBLE)
                val bdLocation = message.bdLocation!!
                val locData = MyLocationData.Builder()
                    .accuracy(bdLocation.radius)
                    .latitude(bdLocation.latitude)
                    .longitude(bdLocation.longitude).build()
                view!!.showMyLocation(locData)
                val mLatitude = bdLocation.latitude
                val mLongitude = bdLocation.longitude
                val currentLatLng = LatLng(mLatitude, mLongitude)
                // 是否第一次定位
                if (isFirstLoc) {
                    index = Constants.NUMBER_ZERO
                    isFirstLoc = false
                    // 实现动画跳转
                    val u = MapStatusUpdateFactory.newLatLngZoom(currentLatLng, 16f)
                    view!!.animateMapStatus(u)
                    /**
                     * 搜索位置点周边POI
                     */
                    searchPoiAddressInfo(currentLatLng)
                    return
                }
            }
            Constants.NUMBER_ONE->{
                view!!.toast(R.string.location_failed)
                view!!.hintOrShowMarker(View.GONE)
            }
        }
    }

    /**
     * 绑定view
     */
    override fun attachView(view: SearchAddressController.View) {
        this.view = view
        if (searchPoi == null) {
            searchPoi = PoiSearch.newInstance()
        }
        searchPoi!!.setOnGetPoiSearchResultListener(this)
    }

    /**
     * 解绑View
     */
    override fun detachView() {
        stopLocation()
        isFirstLoc = true
        isTouch = true
        searchPoi?.destroy()
        searchPoi = null
        view = null
    }

    override fun onMapStatusChangeStart(p0: MapStatus?) {

    }

    override fun onMapStatusChangeStart(p0: MapStatus?, p1: Int) {

    }

    override fun onMapStatusChange(p0: MapStatus?) {

    }

    override fun onMapStatusChangeFinish(p0: MapStatus?) {
        if (isTouch) {
            poiAddressList!!.clear()
            val latLng: LatLng = p0!!.target
            searchPoiAddressInfo(latLng)
        }
    }

    override fun onTouch(p0: MotionEvent?) {
        isTouch = true
    }

    override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {

    }

    @SuppressLint("CheckResult")
    override fun onGetPoiResult(p0: PoiResult?) {
        view!!.hintLoadDialog()
        if (p0 == null || p0.error != SearchResult.ERRORNO.NO_ERROR) {
            return
        }
        if (!isTouch) {
            return
        }
        val allPoi = p0.allPoi
        if (!allPoi.isNullOrEmpty()) {
            Observable.fromIterable(allPoi)
                .compose(SchedulerProvider.instance.applySchedulers())
                .map { info ->
                    val address = info.address
                    val city = info.city
                    val latLng = info.location
                    LocationAddressBean(
                        latLng.latitude,
                        latLng.longitude,
                        city,
                        info.name,
                        address,
                        null
                    )
                }
                .subscribe {
                    if (!poiAddressList!!.contains(it)) {
                        poiAddressList!!.add(it)
                    }
                    if (!poiAddressList.isNullOrEmpty()) {
                        defaultAddress = poiAddressList!![0]
                    }
                    view!!.updateListView()
                }
        }
    }

    override fun onGetPoiDetailResult(p0: PoiDetailResult?) {

    }

    override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {

    }

    /**
     * 搜索poi地址
     */
    private fun searchPoiAddressInfo(latLng: LatLng) {
        view!!.showLoadDialog()
        val searchOption = PoiNearbySearchOption()
        searchOption.keyword(searchText)
        searchOption.location(latLng)
        searchOption.radius(50000)
        searchOption.sortType = PoiSortType.distance_from_near_to_far
        searchOption.pageNum(index)
        searchOption.pageCapacity(Constants.PAGE_SIZE)
        val builder = PoiFilter.Builder()
        builder.industryType(PoiFilter.IndustryType.LIFE)
        builder.sortName(PoiFilter.SortName.LifeSortName.DISTANCE)
        val filter = builder.build()
        searchOption.poiFilter(filter)
        searchPoi?.searchNearby(searchOption)
    }
}