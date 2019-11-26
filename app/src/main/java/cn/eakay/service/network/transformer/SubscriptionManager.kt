package cn.eakay.service.network.transformer

import io.reactivex.disposables.Disposable
import java.util.*
import io.reactivex.disposables.CompositeDisposable


/**
 * @packageName: UserService
 * @fileName: SubscriptionManager
 * @author: chitian
 * @date: 2019-07-18 09:27
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SubscriptionManager :
    SubscriptionHelper<Objects> {

    companion object {
        val instance: SubscriptionManager =
            SubscriptionManager()
        private val mDisposables: CompositeDisposable = CompositeDisposable()
    }

    override fun add(subscription: Disposable) {
        mDisposables.add(subscription)
    }

    override fun cancel(subscription: Disposable) {
        mDisposables.delete(subscription)
    }

    override fun cancelAll() {
        mDisposables.clear()
    }
}