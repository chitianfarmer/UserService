package cn.eakay.service.base

/**
 * @packageName: UserService
 * @fileName: Constants
 * @author: chitian
 * @date: 2019-07-11 08:25
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
object Constants {
    /**
     * 请求的数据格式
     */
    val DATA_FORMAT = "application/json"
    /**
     * 生成参数签名的密钥
     */
    val APP_SECRET = "b50229eba6c397b7f71c73baf3a79d8b"
    /**
     * 分配给用户的app_key,标识应用
     */
    val APP_KEY = "56350671685"
    /**
     * 退款流程传递的key
     */
    var KEY_REFUND_REMARKS = "KEY_REFUND_REMARKS"
    val KEY_ORDER_ID = "KEY_ORDER_ID"
    val KEY_ORDER_TYPE = "KEY_ORDER_TYPE"
    val KEY_CAR_ID = "KEY_CAR_ID"
    /**
     * 自定义的type
     */
    var KEY_TYPE = "type"
    /**
     * 错误码状态 1201040005 其他地方登陆  1201040006 无效的账户
     * 120160001 验证码失效 1201040001 Token失效   1201050001  无效的API地址  1201050002 无效的APP
     * 0 成功 其余均为出错 1201020001 无效的邀请码  -1 数据转换错误 -2 数据为空 1201010003 身份证格式错误
     * 1201060020 下单验证码错误 1201060009 更新银行卡时间不到
     */
    val KEY_SUCCESS_DATAS = "datas"
    val KEY_REQUEST_FAILED_CODE = "1"
    val KEY_REQUEST_SUCCESSED_CODE = "0"
    val KEY_REQUEST_DATAS_CHANGE_CODE = "-1"
    val KEY_REQUEST_DATAS_EMPTY_CODE = "-2"

    val KEY_REQUEST_LOGIN_OTHER_CODE = "1201040005"
    val KEY_REQUEST_USER_NOT_EXIT_CODE = "1201040006"
    val KEY_REQUEST_WRONG_VERIFY_CODE = "1201060001"
    val KEY_REQUEST_TOKEN_CODE = "1201040001"
    val KEY_REQUEST_API_CODE = "1201050001"
    val KEY_REQUEST_APP_CODE = "1201050002"
    val KEY_REQUEST_INVITED_CODE = "1201020001"
    val KEY_REQUEST_ORDER_INVITED_CODE = "1201060020"
    val KEY_REQUEST_ERROR_IDCARD_CODE = "1201010003"
    val KEY_UPDATE_ERROR_CODE = "1201060009"
    val KEY_USER_ACOUNT_WORNG_CODE = "1101060047"
    /*网络错误码*/
    val KEY_REQUEST_418_CODE = "418"
    val KEY_REQUEST_500_CODE = "500"
    val KEY_REQUEST_501_CODE = "501"
    val KEY_REQUEST_502_CODE = "502"
    val KEY_REQUEST_503_CODE = "503"
    /**
     * 0.00
     */
    var FREE_BY_ZERO = "0.00"
    /**
     * 手机号传递
     */
    var KEY_MOBILE = "KEY_MOBILE"
    /**
     * 密码传递
     */
    var KEY_PASSWORD = "KEY_PASSWORD"
    /**
     * 从哪个页面过来的数据 0 是登录页面 1 是注册页面 2 是其他页面
     */
    var KEY_FROM_TYPE = "KEY_FROM_TYPE"
    /**
     * 传递一个object对象的key
     */
    var KEY_OBJECT = "KEY_OBJECT"
    /**
     * 获取deviceToken/accessToken的key
     */
    var KEY_REQUEST_DEVICE_TOKEN = "deviceToken"
    var KEY_REQUEST_ACCESS_TOKEN = "accessToken"
    /**
     * 获取错误码的key
     */
    var KEY_REQUEST_ERROR_CODE = "errCode"
    var KEY_REQUEST_ERROR_MSG = "errMsg"
    /**
     * 默认的初始page
     */
    var PAGE_COUNT = 1
    /**
     * 默认的显示条目
     */
    var PAGE_SIZE = 10
    /**
     * 数字
     */
    var NUMBER_ZERO = 0
    var NUMBER_ONE = 1
    var NUMBER_TWO = 2
    var NUMBER_THREE = 3
    var NUMBER_FOUR = 4
    var NUMBER_FIVE = 5
    var NUMBER_SIX = 6
    var NUMBER_SEVEN = 7
    var NUMBER_EIGHT = 8
    var NUMBER_NINE = 9
    var NUMBER_TEN = 10
    var NUMBER_ELEVEN = 11
    var NUMBER_TWELVE = 12
    val DEFAULT_VALUE = 50
    /**
     * 是否显示或者隐藏fragment
     */
    val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"
    /**
     * key of url
     */
    val KEY_OF_URL = "url"

    val SP_NAME = "eakay_sales_sp"

    /**
     * 保存用户TOKEN的key
     */
    val KEY_AUTN_TOKEN = "key_auth_token"
    /**
     * 保存用户DEVICETOKEN的key
     */
    val KEY_DEVICE_TOKEN = "key_device_token"
    /**
     * 保存用户是否登录的登录信息
     */
    val KEY_IS_USER_LOGIN = "key_is_user_login"
    /**
     * 保存用户是否工作的信息
     */
    val KEY_IS_USER_WORK = "key_is_user_work"
    /**
     * 自动填充账户名密码
     */
    val KEY_AUTO_FILL_ACCOUNT = "key_auto_fill_account_in_signin"
    val USERINFO_SP_NAME = "eakay_info"
    val KEY_TEL_NUM = "telephone"
    val KEY_USER_IDS = "user_id"
    val KEY_USER_ACCOUNT = "user_account"

}