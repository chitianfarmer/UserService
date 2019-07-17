package cn.eakay.service.network

import android.text.TextUtils
import cn.eakay.service.base.Constants
import com.alibaba.fastjson.JSONObject
import com.changyoubao.vipthree.base.LSPUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.util.HashMap

/**
 * @packageName: UserService
 * @fileName: HeaderInterceptor
 * @author: chitian
 * @date: 2019-07-16 10:14
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class HeaderInterceptor : Interceptor {

    private val mediaType: MediaType = "application/json; charset=utf-8".toMediaTypeOrNull()!!

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain!!.request()
        val builder: Request.Builder = request.newBuilder()
        var token = LSPUtils.get(Constants.KEY_AUTN_TOKEN, "")
        builder.addHeader("Accept", Constants.DATA_FORMAT)
        builder.addHeader("Content-Type", Constants.DATA_FORMAT)
        builder.addHeader("appId", Constants.APP_KEY)
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader(Constants.KEY_REQUEST_ACCESS_TOKEN, token)
        }
        return chain.proceed(methodPost(request, builder))
    }
    private fun methodPost(request: Request, builder: Request.Builder): Request {
        val jsonObject = JSONObject()
        var requestBody = request.body
        //有好几种body
        when (requestBody) {
            is FormBody -> {
                val oldBody = request.body as FormBody?
                for (i in 0 until oldBody!!.size) {
                    jsonObject[oldBody.encodedName(i)] = oldBody.encodedValue(i)
                }
                requestBody = RequestBody.create(mediaType, jsonObject.toJSONString())
            }
        }
        return builder.post(requestBody!!).build()
    }
}