package cn.eakay.service.network.transformer

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @packageName: UserService
 * @fileName: SchedulerProvider
 * @author: chitian
 * @date: 2019-11-27 09:37
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SchedulerProvider : BaseSchedulerProvider {
    companion object{
        val instance = SchedulerProvider()
    }

    override fun computation(): Scheduler = Schedulers.computation()

    override fun io(): Scheduler=Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun <T> applySchedulers(): ObservableTransformer<T, T> =
        ObservableTransformer { observable: Observable<T> ->
            observable.subscribeOn(
                io()
            ).observeOn(ui())
        }
}