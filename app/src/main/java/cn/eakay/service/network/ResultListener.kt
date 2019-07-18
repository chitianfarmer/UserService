package cn.eakay.service.network


/**
 * @packageName: UserService
 * @fileName: ResultListener
 * @author: chitian
 * @date: 2019-07-18 10:37
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
interface ResultListener<T> {
    fun success(result: T)
    fun failed(error: Throwable?)
}