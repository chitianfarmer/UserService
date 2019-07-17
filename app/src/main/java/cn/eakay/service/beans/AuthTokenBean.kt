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
data class AuthTokenBean(
    val accessToken: String,
    val appId: String,
    val datas: Any,
    val errCode: String,
    val errMsg: String,
    val expireIn: String
)