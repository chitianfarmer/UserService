package cn.eakay.service.network

import cn.eakay.service.beans.AuthTokenBean
import cn.eakay.service.beans.DeviceTokenBean
import cn.eakay.service.beans.LoginAndRegisterBean
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
    @POST("auth/oauthPrivateTokenLogin")
    fun signIn(@Body body: RequestBody): Observable<LoginAndRegisterBean>

    @POST("auth/getToken")
    fun checkNoLoginAuthToken(@Body body: RequestBody): Observable<AuthTokenBean>

    @POST("auth/app/initializeDevice")
    fun getDeviceToken(@Body body: RequestBody): Observable<DeviceTokenBean>
}