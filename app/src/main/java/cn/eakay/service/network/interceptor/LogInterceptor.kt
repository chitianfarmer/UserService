package cn.eakay.service.network.interceptor

import com.alibaba.fastjson.JSONObject
import com.shs.easywebviewsupport.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException

/**
 * @packageName: UserService
 * @fileName: LogInterceptor
 * @author: chitian
 * @date: 2019-11-26 11:28
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class LogInterceptor : Interceptor {
    private val POST = "POST"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val startTime = System.currentTimeMillis()
        LogUtils.loge("\n")
        LogUtils.loge("----------------Start----------------")
        LogUtils.loge("\n")
        LogUtils.loge("| 请求地址信息")
        LogUtils.loge("\n")
        LogUtils.loge("| " + request.url.toString())
        LogUtils.loge("\n")
        LogUtils.loge("| 请求头部信息")
        LogUtils.loge("\n")
        val headers = request.headers
        val size = headers.size
        val headersParams = JSONObject()
        for (i in 0 until size) {
            headersParams[headers.name(i)] = headers.value(i)
        }
        LogUtils.loge("| " + headersParams.toJSONString())
        LogUtils.loge("\n")
        LogUtils.loge("| 请求方法信息")
        LogUtils.loge("\n")
        LogUtils.loge("| " + request.method)
        val method = request.method
        if (POST == method) {
            val buffer = Buffer()
            val body = request.body
            body!!.writeTo(buffer)
            val type = body.contentType()
            val params = buffer.readString(type!!.charset()!!)
            LogUtils.loge("\n")
            LogUtils.loge("| 请求参数信息")
            LogUtils.loge("\n")
            LogUtils.loge("| $params")
        }
        val source = response.body!!.source()
        source.request(Long.MAX_VALUE)
        val result = source.buffer.clone().readUtf8()
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        LogUtils.loge("\n")
        LogUtils.loge("| 返回数据信息")
        LogUtils.loge("\n")
        LogUtils.loge("| " + JSONObject.parseObject(result).toJSONString())
        LogUtils.loge("\n")
        LogUtils.loge("-------------End:" + duration + "毫秒---------------")
        return response
    }
}