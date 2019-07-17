package cn.eakay.service.base

import android.app.Activity
/**
 * @packageName: UserService
 * @fileName: BaseView
 * @author: chitian
 * @date: 2019-07-09 15:18
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
interface BaseView {
    /**
     * toast
     *
     * @param msg
     */
    fun toast(msg: Any)

    /**
     * 显示弹窗
     */
    fun showLoadDialog()

    /**
     * 隐藏弹窗
     */
    fun hintLoadDialog()

    /**
     * 获取依赖的activity
     *
     * @return
     */
    fun getBaseActivity(): Activity
}