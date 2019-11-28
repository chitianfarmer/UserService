package cn.eakay.service.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.base.EakayApplication
import cn.eakay.service.beans.messages.LocationMessage
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.model.LatLng
import com.shs.easywebviewsupport.utils.LogUtils
import org.greenrobot.eventbus.EventBus

/**
 * @packageName: UserService
 * @fileName: BdLocationHelper
 * @author: chitian
 * @date: 2019-07-11 14:47
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class BdLocationHelper {

    private var helper: BdLocationHelper? = null

    /**
     * 定位启动器
     */
    private var mLocationClient: LocationClient? = null
    /**
     * 定位监听
     */
    private var listener: EakayBdLocationListener? = null

    companion object {
        @Synchronized
        fun getInstance(): BdLocationHelper {
            return BdLocationHelper()
        }
    }

    constructor() {
        initClient()
        initMapClientOpts()
    }

    private fun initClient() {
        if (mLocationClient == null) {
            mLocationClient = LocationClient(EakayApplication.instance!!)
        }
        if (listener == null) {
            listener = EakayBdLocationListener()
        }
    }

    private fun initMapClientOpts() {
        val option = LocationClientOption()
        // 设置定位模式
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //打开gps
        option.isOpenGps = true
        // 返回的定位结果是百度经纬度，默认值gcj02
        option.setCoorType("bd09ll")
        // 设置发起定位请求的间隔时间为20000ms
        option.setScanSpan(5000)
        // 返回的定位结果包含地址信息
        option.setIsNeedAddress(true)
        // 返回的定位结果包含手机机头的方向
        option.setNeedDeviceDirect(true)
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.isLocationNotify = true
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true)
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false)
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false)
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false)
        /*打开位置变化自动回调*/
        option.setOpenAutoNotifyMode(5000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT)
        mLocationClient!!.locOption = option
        // 注册监听函数
        mLocationClient!!.registerLocationListener(listener)
    }

    /**
     * 开始定位
     */
    fun startLocation() {
        if (mLocationClient != null) {
            mLocationClient!!.start()
        }
    }

    /**
     * 停止定位
     */
    fun stopLocation() {
        if (mLocationClient != null) {
            mLocationClient!!.unRegisterLocationListener(listener)
            mLocationClient!!.stop()
            mLocationClient = null
            listener = null
            helper = null
        }
    }

    /**
     * 定位监听
     */
    private inner class EakayBdLocationListener : BDAbstractLocationListener() {

        override fun onReceiveLocation(bdLocation: BDLocation?) {
            if (bdLocation == null) {
                return
            }
            LogUtils.loge("----定位成功:" + bdLocation.addrStr)
            if (isLocSuccessfully(bdLocation)) {
                EakayApplication.instance!!.setLocationCity(bdLocation.city)
                EakayApplication.instance!!.setCityCode(bdLocation.cityCode)
                val latLng = LatLng(bdLocation.latitude, bdLocation.longitude)
                EakayApplication.instance!!.setLocationLatlng(latLng)
                EventBus.getDefault().post(LocationMessage(Constants.NUMBER_ZERO, bdLocation))
            } else {
                EventBus.getDefault().post(LocationMessage(Constants.NUMBER_ONE, null))
            }
            /**
             * 避免多次定位 调用stop方法
             */
            if (mLocationClient != null) {
                mLocationClient!!.stop()
            }
        }
    }

    fun isLocSuccessfully(location: BDLocation): Boolean {
        return location.locType != BDLocation.TypeCriteriaException &&
                location.locType != BDLocation.TypeNetWorkException &&
                location.locType != BDLocation.TypeServerError
    }

    /**
     * 检查系统是否开启了位置服务
     */
    fun isLocationEnabled(ctx: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return try {
                val locationMode =
                    Settings.Secure.getInt(
                        ctx.applicationContext.contentResolver,
                        Settings.Secure.LOCATION_MODE
                    )
                locationMode != Settings.Secure.LOCATION_MODE_OFF
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                false
            }

        } else {
            val locationProviders = Settings.Secure.getString(
                ctx.applicationContext.contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            return !TextUtils.isEmpty(locationProviders)
        }
    }
}