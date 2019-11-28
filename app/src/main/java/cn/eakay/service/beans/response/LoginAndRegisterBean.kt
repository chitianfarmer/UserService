package cn.eakay.service.beans.response

import cn.eakay.service.beans.base.BaseResponse
import java.io.Serializable

/**
 * @packageName: UserService
 * @fileName: LoginAndRegisterBean
 * @author: chitian
 * @date: 2019-07-16 09:03
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class LoginAndRegisterBean : BaseResponse() {

    /**
     * datas : {"loginStatus":"1","loginStatusComment ":"register:0,login:1"}
     */

    private var datas: LoginBean? = null

    fun getDatas(): LoginBean? {
        return datas
    }

    fun setDatas(datas: LoginBean) {
        this.datas = datas
    }

    class LoginBean : Serializable {
        /**
         * loginStatus : 1
         * loginStatusComment  : register:0,login:1
         */

        var loginStatus: String? = null
        var loginStatusComment: String? = null
    }
}