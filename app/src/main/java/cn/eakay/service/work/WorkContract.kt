package cn.eakay.service.work

import cn.eakay.service.base.BasePresenter
import cn.eakay.service.base.BaseView

/**
 * @packageName: UserService
 * @fileName: WorkContract
 * @author: chitian
 * @date: 2019-07-18 16:03
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
interface WorkContract {

    interface View : BaseView {
        /**
         * get input job number for once
         *
         * @return job number 1
         */
        fun getInputWorkerFirst(): String?

        /**
         * get input job number for again
         *
         * @return job number 2
         */
        fun getInputWorkerSecond(): String?

        /**
         * show the request error
         *
         * @param error
         */
        fun showError(error: String)

        /**
         * hint the error view
         */
        fun hintErrorView()

    }

    interface Presenter : BasePresenter<View> {
        /**
         * go to work
         */
        fun go2Work()

        /**
         * go to main activity
         */
        fun toMain()
    }
}