package cn.eakay.service.network.interceptor

import cn.eakay.service.base.Constants
import com.changyoubao.vipthree.base.LSPUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

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

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain!!.request()
        val builder: Request.Builder = request.newBuilder()
        var token = LSPUtils.get(Constants.KEY_AUTN_TOKEN, "")
        builder.addHeader("Accept", Constants.DATA_FORMAT)
        builder.addHeader("Content-Type", Constants.DATA_FORMAT)
        builder.addHeader("appId", Constants.APP_KEY)
        if (token.isNotEmpty()) {
            builder.addHeader(Constants.KEY_REQUEST_ACCESS_TOKEN, token)
        }
        val build = builder.build()
        return chain.proceed(build)
    }
}