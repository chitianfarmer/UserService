package cn.eakay.service.network

import cn.eakay.service.beans.*
import com.alibaba.fastjson.JSONObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @packageName: UserService
 * @fileName: ApiService
 * @author: chitian
 * @date: 2019-07-10 10:14
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
interface ApiService {
    /*登陆*/
    @POST("auth/oauthPrivateTokenLogin")
    fun signIn(@Body body: RequestBody): Observable<LoginAndRegisterBean>

    /*获取token*/
    @POST("auth/getToken")
    fun checkNoLoginAuthToken(@Body body: RequestBody): Observable<AuthTokenBean>

    /*刷新登陆的token*/
    @POST("auth/getToken")
    fun refreshLoginAuthToken(@Body body: RequestBody): Observable<AuthTokenBean>

    /*获取deviceId*/
    @POST("auth/app/initializeDevice")
    fun getDeviceToken(@Body body: RequestBody): Observable<DeviceTokenBean>

    /*获取列表*/
    @POST("serviceOrderApi/getServiceOrderListToAppByStatus")
    fun requestOrderList(@Body body: RequestBody): Observable<TabOrderListBean>

    /*上班*/
    @POST("serviceUserLoginApi/insertServiceUserLoginRecord")
    fun onLineWork(@Body body: RequestBody): Observable<WorkBean>

    /*下班*/
    @POST("serviceUserLoginApi/updateServiceUserLoginRecord")
    fun offLineWork(@Body body: RequestBody): Observable<JSONObject>

    /*订单详情*/
    @POST("serviceOrderApi/getServiceOrderInfoById")
    fun getOrderDetailInfo(@Body body: RequestBody): Observable<OrderDetailBean>
}