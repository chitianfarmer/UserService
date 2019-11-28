package cn.eakay.service.beans.base

import java.io.Serializable

/**
 * @packageName: UserService
 * @fileName: BaseResponse
 * @author: chitian
 * @date: 2019-07-16 09:00
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
open class BaseResponse : Serializable {
    private var errCode: String? = null
    private val errMsg: String? = null
    private var page: PageBean? = null
    private var sort: Any? = null

    fun getErrCode(): String? {
        return errCode
    }

    fun setErrCode(responseCode: String) {
        this.errCode = responseCode
    }

    fun getErrMsg(): String? {
        return errMsg
    }

    fun setErrMsg(responseMsg: String) {
        this.errCode = responseMsg
    }

    fun getPage(): PageBean? {
        return page
    }

    fun setPage(page: PageBean) {
        this.page = page
    }

    fun getSort(): Any? {
        return sort
    }

    fun setSort(sort: Any) {
        this.sort = sort
    }


    class PageBean : Serializable {
        /**
         * currentPage : 1
         * pageSize : 10
         * totalCount : 2
         * totalPages : 1
         */

        var currentPage: Int = 0
        var pageSize: Int = 0
        var totalCount: Int = 0
        var totalPages: Int = 0
    }

}