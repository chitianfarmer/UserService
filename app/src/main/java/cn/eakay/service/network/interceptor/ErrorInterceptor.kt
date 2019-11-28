package cn.eakay.service.network.interceptor

import android.text.TextUtils
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.base.EakayApplication
import cn.eakay.service.beans.messages.ErrorMessages
import cn.eakay.service.beans.messages.OtherLoginMessage
import com.alibaba.fastjson.JSONObject
import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus

/**
 * @packageName: UserService
 * @fileName: ErrorInterceptor
 * @author: chitian
 * @date: 2019-11-26 11:30
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val response = chain.proceed(oldRequest)
        val source = response.body!!.source()
        val result = source.buffer.clone().readUtf8()
        val resultObject = JSONObject.parseObject(result)
        val errorCode = resultObject.getString(Constants.KEY_REQUEST_ERROR_CODE)
        val errorMSg = resultObject.getString(Constants.KEY_REQUEST_ERROR_MSG)
        if (TextUtils.equals(Constants.KEY_REQUEST_SUCCESS_CODE, errorCode) ||
            TextUtils.equals(Constants.KEY_REQUEST_TOKEN_CODE, errorCode)) {
            return response
        }
        if (Constants.KEY_REQUEST_LOGIN_OTHER_CODE == errorCode) {
            EventBus.getDefault().post(
                OtherLoginMessage(
                    Constants.KEY_REQUEST_LOGIN_OTHER_CODE, EakayApplication.instance!!.getString(
                        R.string.reLogin
                    )
                )
            )
        } else {
            EventBus.getDefault().post(
                ErrorMessages(
                    errorCode,
                    errorMSg
                )
            )
            return response
        }
        return response
    }
}