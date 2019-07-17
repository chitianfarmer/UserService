package cn.eakay.service.sign

import cn.eakay.service.base.BasePresenter
import cn.eakay.service.base.BaseView

/**
 * @packageName: UserService
 * @fileName: SignInContract
 * @author: chitian
 * @date: 2019-07-10 14:01
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SignInContract {
    interface View : BaseView {
        /**
         * 获取输入的账户
         */
        fun getInputAccount(): String

        /**
         * 获取输入的密码
         */
        fun getInputPassword(): String

        /**
         * 设置密码错误提示
         */
        fun setPassWordError(msg: Any?, isError: Boolean)

        /**
         * 设置账户错误提示
         */
        fun setAccountError(msg: Any?, isError: Boolean)

        /**
         * 清除错误提示
         */
        fun clearError()
    }

    interface Presenter : BasePresenter<View> {
        /**
         * submit to login
         */
        fun submitToLogin()

        /**
         * jump to work page
         */
        fun jumpToWorkView()
    }
}