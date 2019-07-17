package cn.eakay.service.network

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class RequestCallback<T>(private val context: Context) : Observer<ResponseWrapper<T>> {
    private var isShowLoading: Boolean = true

    constructor(context: Context, isShowLoading: Boolean) : this(context) {
        this.isShowLoading = isShowLoading
    }

    abstract fun success(data: T)
    abstract fun failure(statusCode: Int, apiErrorModel: ApiErrorModel)

    private object Status {
        const val SUCCESS = 200
    }

    override fun onSubscribe(d: Disposable) {
        if (isShowLoading) {
//            LoadingDialog.show(context)
        }
    }

    override fun onNext(t: ResponseWrapper<T>) {
        if (t.status == Status.SUCCESS) {
            success(t.data)
            return
        }

        val apiErrorModel: ApiErrorModel = when (t.status) {
            ApiErrorType.INTERNAL_SERVER_ERROR.status ->
                ApiErrorType.INTERNAL_SERVER_ERROR.getApiErrorModel(context)
            ApiErrorType.BAD_GATEWAY.status ->
                ApiErrorType.BAD_GATEWAY.getApiErrorModel(context)
            ApiErrorType.NOT_FOUND.status ->
                ApiErrorType.NOT_FOUND.getApiErrorModel(context)
            else -> ApiErrorModel(t.status, t.message)
        }
        failure(t.status, apiErrorModel)
    }

    override fun onComplete() {
//        LoadingDialog.cancel()
    }

    override fun onError(e: Throwable) {
//        LoadingDialog.cancel()
        println(e)
        val apiErrorType: ApiErrorType = when (e) {
            is UnknownHostException -> ApiErrorType.NETWORK_NOT_CONNECT
            is ConnectException -> ApiErrorType.NETWORK_NOT_CONNECT
            is SocketTimeoutException -> ApiErrorType.CONNECTION_TIMEOUT
            else -> ApiErrorType.UNEXPECTED_ERROR
        }
        failure(apiErrorType.status, apiErrorType.getApiErrorModel(context))
    }
}
