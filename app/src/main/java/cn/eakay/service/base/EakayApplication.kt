package cn.eakay.service.base

import android.app.Activity
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDexApplication
import cn.eakay.service.network.ApiUtils
import cn.eakay.service.utils.StringUtils
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.model.LatLng
import com.changyoubao.vipthree.base.LSPUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator
import com.scwang.smartrefresh.layout.api.RefreshLayout

/**
 * @packageName: UserService
 * @fileName: EakayApplication
 * @author: chitian
 * @date: 2019-07-09 15:35
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class EakayApplication : MultiDexApplication() {
    private var locationLatlng: LatLng? = null
    /**
     * 定位城市
     */
    private var locationCity: String? = null
    private var cityCode: String? = null

    override fun onCreate() {
        super.onCreate()
        initThird()
    }

    private fun initThird() {
        ApiUtils.instance.init()
        LSPUtils.init(this, Constants.SP_NAME)
        // 百度地图初始化组件.
        SDKInitializer.initialize(this)
        //设置使用https请求
        SDKInitializer.setHttpsEnable(true)
    }

    init {
        instance = this
        activityList = ArrayList()
    }

    companion object {
        var activityList: ArrayList<Activity>? = null
        var instance: EakayApplication? = null
    }

    fun addActivity(activity: Activity) {
        activityList!!.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityList!!.remove(activity)
    }

    fun finishActivity(activity: Activity) {
        for (i in activityList!!.indices) {
            val exitActivity = activityList!![i]
            if (exitActivity == activity) {
                exitActivity.finish()
            }
        }
    }

    fun finishAllActivity() {
        for (i in activityList!!.indices) {
            activityList!![i].finish()
        }
    }

    fun getLocationLatlng(): LatLng? {
        return locationLatlng
    }

    fun setLocationLatlng(locationLatlng: LatLng?) {
        this.locationLatlng = locationLatlng
    }

    fun setCityCode(cityCode: String?) {
        this.cityCode = cityCode
    }

    fun getCityCode(): String? {
        return cityCode
    }

    fun setLocationCity(city: String?) {
        if (!StringUtils.isEmpty(city)) {
            locationCity = city
        }
    }

    fun getLocationCity(): String? {
        return locationCity
    }
}