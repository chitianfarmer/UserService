package cn.eakay.service.base


/**
 * @packageName: UserService
 * @fileName: BasePresenter
 * @author: chitian
 * @date: 2019-07-09 15:16
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
interface BasePresenter<T : BaseView> {
    /**
     * 绑定view
     */
    fun attachView(view : T)

    /**
     * 解绑View
     */
    fun detachView()
}