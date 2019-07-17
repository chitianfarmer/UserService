package cn.eakay.service.network

import android.annotation.SuppressLint
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.base.EakayApplication
import cn.eakay.service.beans.OtherLoginMessage
import cn.eakay.service.utils.SecurityUtils
import cn.eakay.service.utils.StringUtils
import com.alibaba.fastjson.JSONObject
import com.changyoubao.vipthree.base.LSPUtils
import com.shs.easywebviewsupport.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.greenrobot.eventbus.EventBus


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
        val newRequest = getNewRequest(oldRequest, false)
        var response = chain.proceed(newRequest)
        val body = response.body
        val resp = body?.bytes()
        val string = resp.toString()
        val jsonObject = JSONObject.parseObject(string)
        when (jsonObject.getString(Constants.KEY_REQUEST_ERROR_CODE)) {
            Constants.KEY_REQUEST_LOGIN_OTHER_CODE -> {
                EventBus.getDefault().post(
                    OtherLoginMessage(
                        Constants.KEY_REQUEST_LOGIN_OTHER_CODE, EakayApplication.instance!!.getString(
                            R.string.reLogin
                        )
                    )
                )
            }
            Constants.KEY_REQUEST_TOKEN_CODE -> {
                refreshToken()
                val build = getNewRequest(oldRequest, true)
                response = chain.proceed(build)
            }
        }
        return response
    }

    /**
     * 添加公共参数
     *
     * @param oldRequest
     * @return
     */
    private fun getNewRequest(oldRequest: Request, refreshToken: Boolean): Request {
        val newHeaders = oldRequest.headers.newBuilder().build()
        val builder = oldRequest.url
            .newBuilder()
        if (refreshToken) {
            token = LSPUtils.get(Constants.KEY_AUTN_TOKEN, "")
        }
        return oldRequest.newBuilder()
            .headers(newHeaders)
            .header(Constants.KEY_REQUEST_ACCESS_TOKEN, token!!)
            .method(oldRequest.method, oldRequest.body)
            .url(builder.build())
            .build()
    }

    @SuppressLint("CheckResult")
    private fun refreshToken() {
        val deviceToken = LSPUtils.get(Constants.KEY_DEVICE_TOKEN, "")
        val timeMillis = System.currentTimeMillis()
        val param = JSONObject()
        param["appId"] = Constants.APP_KEY
        param["secret"] = Constants.APP_SECRET
        param["timestamp"] = timeMillis
        param["deviceToken"] = deviceToken
        param["sign"] = SecurityUtils.MD5(Constants.APP_KEY + Constants.APP_SECRET + timeMillis)
        val body = StringUtils.createBody(param)
        val authToken = ApiUtils.instance.service.checkNoLoginAuthToken(body)
        authToken.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                val errCode = result.getErrCode()
                val errMsg = result.getErrMsg()
                val accessToken = result.getAccessToken()
                when (errCode) {
                    "0" -> {
                        token = accessToken
                        LSPUtils.put(Constants.KEY_AUTN_TOKEN, accessToken)
                    }
                    else -> {
                        LogUtils.loge("请求失败，错误码：$errCode，错误信息：$errMsg")
                    }
                }
            }, { error ->
                run {
                    val message = error.message
                    LogUtils.loge("请求失败：$message")
                }
            })
    }
}