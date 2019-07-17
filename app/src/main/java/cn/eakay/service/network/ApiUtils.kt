package cn.eakay.service.network

import cn.eakay.service.BuildConfig
import io.reactivex.internal.schedulers.NewThreadScheduler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @packageName: UserService
 * @fileName: ApiUtils
 * @author: chitian
 * @date: 2019-07-10 10:00
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class ApiUtils private constructor() {
    lateinit var service: ApiService

    private object ApiHolder {
        val INSTANCE = ApiUtils()
    }

    companion object {
        val instance by lazy { ApiHolder.INSTANCE }
    }

    fun init() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .connectTimeout(20000, TimeUnit.MILLISECONDS)
            .readTimeout(20000, TimeUnit.MILLISECONDS)
            .writeTimeout(20000, TimeUnit.MILLISECONDS)
            /*失败重连*/
            .retryOnConnectionFailure(true)
            /*添加拦截器*/
            .addInterceptor(interceptor)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(TokenInterceptor())
            .build()
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BuildConfig.URL_HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
        service = retrofit.create(ApiService::class.java)
    }
}