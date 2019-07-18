package cn.eakay.service.network

import io.reactivex.disposables.Disposable



/**
 * @packageName: UserService
 * @fileName: SubscriptionHelper
 * @author: chitian
 * @date: 2019-07-18 09:26
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
interface SubscriptionHelper<T> {
    fun add(subscription: Disposable)

    fun cancel(t: Disposable)

    fun cancelAll()
}