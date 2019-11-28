package cn.eakay.service.network.interceptor

import android.annotation.SuppressLint
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.response.AuthTokenBean
import cn.eakay.service.network.ApiUtils
import cn.eakay.service.network.listener.ResultListener
import cn.eakay.service.network.listener.ResultObserver
import cn.eakay.service.network.transformer.SchedulerProvider
import cn.eakay.service.utils.ErrorManager
import cn.eakay.service.utils.MD5Utils
import cn.eakay.service.utils.StringUtils
import com.alibaba.fastjson.JSONObject
import com.changyoubao.vipthree.base.LSPUtils
import com.shs.easywebviewsupport.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


/**
 * @packageName: UserService
 * @fileName: TokenInterceptor
 * @author: chitian
 * @date: 2019-07-16 10:41
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class TokenInterceptor : Interceptor {
    private var token: String? = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val newRequest = getNewRequest(oldRequest)
        var response = chain.proceed(newRequest)
        val body = response.body
        val source = body?.source()
        source?.request(Long.MAX_VALUE)
        val result = source?.buffer?.clone()?.readUtf8()
        val jsonObject = JSONObject.parseObject(result)
        val errorCode = jsonObject.getString(Constants.KEY_REQUEST_ERROR_CODE)
        if (Constants.KEY_REQUEST_TOKEN_CODE == errorCode) {
            refreshToken()
            val build = getNewRequest(oldRequest)
            return chain.proceed(build)
        }
        return response
    }

    /**
     * 添加公共参数
     *
     * @param oldRequest
     * @return
     */
    private fun getNewRequest(oldRequest: Request): Request {
        val newHeaders = oldRequest.headers.newBuilder().build()
        val builder = oldRequest.url
            .newBuilder()
        token = LSPUtils.get(Constants.KEY_AUTN_TOKEN, "")
        return oldRequest.newBuilder()
            .headers(newHeaders)
            .header(Constants.KEY_REQUEST_ACCESS_TOKEN, token!!)
            .method(oldRequest.method, oldRequest.body)
            .url(builder.build())
            .build()
    }

    @SuppressLint("CheckResult")
    private fun refreshToken() {
        val timeMillis = System.currentTimeMillis()
        val param = JSONObject()
        param["appId"] = Constants.APP_KEY
        param["secret"] = Constants.APP_SECRET
        param["timestamp"] = timeMillis
        val deviceToken = LSPUtils.get(Constants.KEY_DEVICE_TOKEN, "")
        if (deviceToken.isNotEmpty()) {
            param["deviceToken"] = deviceToken
        }
        param["sign"] =
            MD5Utils.MD5(Constants.APP_KEY + Constants.APP_SECRET + (if (deviceToken.isEmpty()) "" else deviceToken) + timeMillis)
        val body = StringUtils.createBody(param)
        val authToken = ApiUtils.instance.service.refreshLoginAuthToken(body)
        authToken.compose(SchedulerProvider.instance.applySchedulers())
            .subscribe(ResultObserver(
                object :
                    ResultListener<AuthTokenBean> {
                    override fun success(result: AuthTokenBean) {
                        val errCode = result.getErrCode()
                        val errMsg = result.getErrMsg()
                        val accessToken = result.getAccessToken()
                        when (errCode) {
                            "0" -> {
                                token = accessToken
                                LSPUtils.put(Constants.KEY_AUTN_TOKEN, accessToken)
                                LogUtils.loge("token过期时刷新的Token：$accessToken")
                            }
                            else -> {
                                val resultError = ErrorManager.checkResultError(errCode, errMsg)
                                LogUtils.loge("token过期时刷新的Token失败：$resultError")
                            }
                        }
                    }

                    override fun failed(error: Throwable?) {
                        val message = error?.message
                        LogUtils.loge("token过期时刷新Token请求失败：$message")
                    }
                }
            ))
    }
}