package cn.eakay.service.beans

/**
 * @packageName: UserService
 * @fileName: AuthTokenBean
 * @author: chitian
 * @date: 2019-07-16 09:01
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class AuthTokenBean : BaseResponse() {
    private var accessToken: String? = null
    private var expireIn: String? = null

    fun getAccessToken(): String? {
        return accessToken
    }

    fun setAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

    fun getExpireIn(): String? {
        return expireIn
    }

    fun setExpireIn(expireIn: String) {
        this.expireIn = expireIn
    }
}