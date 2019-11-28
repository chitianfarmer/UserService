package cn.eakay.service.network.transformer

import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler

/**
 * @packageName: UserService
 * @fileName: BaseSchedulerProvider
 * @author: chitian
 * @date: 2019-11-27 09:37
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
interface BaseSchedulerProvider {
    /**
     * 预计算
     */
    fun computation(): Scheduler

    /**
     * io线程
     */
    fun io(): Scheduler

    /**
     * Ui线程
     */
    fun ui(): Scheduler

    /**
     * 线程切换控制
     */
    fun <T> applySchedulers(): ObservableTransformer<T, T>
}