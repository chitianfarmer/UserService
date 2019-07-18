package cn.eakay.service.network

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @packageName: UserService
 * @fileName: ResultObserver
 * @author: chitian
 * @date: 2019-07-18 10:45
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class ResultObserver<T> : Observer<T> {
    private var resultListener: ResultListener<T>? = null

    constructor(resultListener: ResultListener<T>) {
        this.resultListener = resultListener
    }

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
        SubscriptionManager.instance.add(d)
    }

    override fun onNext(t: T) {
        resultListener?.success(t)
    }

    override fun onError(e: Throwable) {
        resultListener?.failed(e)
    }
}