package cn.eakay.service.address

import cn.eakay.service.adapter.SearchAddressAdapter
import cn.eakay.service.base.BasePresenter
import cn.eakay.service.base.BaseView
import cn.eakay.service.beans.messages.LocationMessage
import cn.eakay.service.beans.response.LocationAddressBean
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatusUpdate
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener

/**
 * @packageName: UserService
 * @fileName: SearchAddressController
 * @author: chitian
 * @date: 2019-11-26 16:08
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SearchAddressController {
    interface View : BaseView, SearchAddressAdapter.OnSearchAddressClickListener,
        android.view.View.OnClickListener {
        /**
         * 刷新view
         */
        fun updateListView()

        /**
         * 显示我的位置
         */
        fun showMyLocation(location: MyLocationData)

        /**
         *
         * 移动地图视角
         */
        fun animateMapStatus(status: MapStatusUpdate)

        /**
         * 请打开定位权限
         */
        fun pleaseEnableYourLocationService()

        /**
         * 请求定位权限
         */
        fun requestLocationPermission()

        /**
         * 显示或者隐藏marker
         */
        fun hintOrShowMarker(flag: Int)

    }

    interface Presenter : BasePresenter<View>, BaiduMap.OnMapStatusChangeListener,
        BaiduMap.OnMapTouchListener, OnGetPoiSearchResultListener {
        /**
         * 开启定位
         */
        fun startLocation()

        /**
         * 结束定位
         */
        fun stopLocation()

        /**
         * 显示搜索地址的view
         */
        fun showSearCheView()

        /**
         * 刷新搜索
         */
        fun refreshSearch()

        /**
         * 加载更多搜索
         */
        fun loadMoreSearch()

        /**
         * 搜搜地址
         */
        fun searchPoiInfo(bean: LocationAddressBean)

        /**
         * 获取搜索到的地址列表
         */
        fun getPoiAddressList(): MutableList<LocationAddressBean>

        /**
         * 地址条目被点击
         */
        fun onPoiAddressClick(position: Int, bean: LocationAddressBean)

        /**
         * 位置变化
         */
        fun onLocationChanged(message: LocationMessage)
    }
}