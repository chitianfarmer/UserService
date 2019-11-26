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
    const val DATA_FORMAT = "application/json"
    /**
     * 生成参数签名的密钥
     */
    const val APP_SECRET = "b50229eba6c397b7f71c73baf3a79d8b"
    /**
     * 分配给用户的app_key,标识应用
     */
    const val APP_KEY = "56350671685"
    /**
     * 退款流程传递的key
     */
    const val KEY_REFUND_REMARKS = "KEY_REFUND_REMARKS"
    const val KEY_ORDER_ID = "KEY_ORDER_ID"
    const val KEY_ORDER_TYPE = "KEY_ORDER_TYPE"
    const val KEY_CAR_ID = "KEY_CAR_ID"
    /**
     * 自定义的type
     */
    const val KEY_TYPE = "type"
    /**
     * 错误码状态 1201040005 其他地方登陆  1201040006 无效的账户
     * 120160001 验证码失效 1201040001 Token失效   1201050001  无效的API地址  1201050002 无效的APP
     * 0 成功 其余均为出错 1201020001 无效的邀请码  -1 数据转换错误 -2 数据为空 1201010003 身份证格式错误
     * 1201060020 下单验证码错误 1201060009 更新银行卡时间不到
     */
    const val KEY_SUCCESS_DATAS = "datas"
    const val KEY_REQUEST_FAILED_CODE = "1"
    const val KEY_REQUEST_SUCCESS_CODE = "0"
    const val KEY_REQUEST_DATA_CHANGE_CODE = "-1"
    const val KEY_REQUEST_DATA_EMPTY_CODE = "-2"

    const val KEY_REQUEST_LOGIN_OTHER_CODE = "1201040005"
    const val KEY_REQUEST_USER_NOT_EXIT_CODE = "1201040006"
    const val KEY_REQUEST_WRONG_VERIFY_CODE = "1201060001"
    const val KEY_REQUEST_TOKEN_CODE = "1201040001"
    const val KEY_REQUEST_API_CODE = "1201050001"
    const val KEY_REQUEST_APP_CODE = "1201050002"
    const val KEY_REQUEST_INVITED_CODE = "1201020001"
    const val KEY_REQUEST_ORDER_INVITED_CODE = "1201060020"
    const val KEY_REQUEST_ERROR_IDCARD_CODE = "1201010003"
    const val KEY_UPDATE_ERROR_CODE = "1201060009"
    const val KEY_USER_ACOUNT_WORNG_CODE = "1104060047"
    const val KEY_USER_ACOUN_TNAME_WORNG_CODE = "1101060047"

    /*网络错误码*/
    const val KEY_REQUEST_418_CODE = "418"
    const val KEY_REQUEST_500_CODE = "500"
    const val KEY_REQUEST_501_CODE = "501"
    const val KEY_REQUEST_502_CODE = "502"
    const val KEY_REQUEST_503_CODE = "503"
    /**
     * 0.00
     */
    const val FREE_BY_ZERO = "0.00"
    /**
     * 手机号传递
     */
    const val KEY_MOBILE = "KEY_MOBILE"
    /**
     * 密码传递
     */
    const val KEY_PASSWORD = "KEY_PASSWORD"
    /**
     * 从哪个页面过来的数据 0 是登录页面 1 是注册页面 2 是其他页面
     */
    const val KEY_FROM_TYPE = "KEY_FROM_TYPE"
    /**
     * 传递一个object对象的key
     */
    const val KEY_OBJECT = "KEY_OBJECT"
    /**
     * 获取deviceToken/accessToken的key
     */
    const val KEY_REQUEST_DEVICE_TOKEN = "deviceToken"
    const val KEY_REQUEST_ACCESS_TOKEN = "accessToken"
    /**
     * 获取错误码的key
     */
    const val KEY_REQUEST_ERROR_CODE = "errCode"
    const val KEY_REQUEST_ERROR_MSG = "errMsg"
    /**
     * 默认的初始page
     */
    const val PAGE_COUNT = 1
    /**
     * 默认的显示条目
     */
    const val PAGE_SIZE = 10
    /**
     * 数字
     */
    const val NUMBER_ZERO = 0
    const val NUMBER_ONE = 1
    const val NUMBER_TWO = 2
    const val NUMBER_THREE = 3
    const val NUMBER_FOUR = 4
    const val NUMBER_FIVE = 5
    const val NUMBER_SIX = 6
    const val NUMBER_SEVEN = 7
    const val NUMBER_EIGHT = 8
    const val NUMBER_NINE = 9
    const val NUMBER_TEN = 10
    const val NUMBER_ELEVEN = 11
    const val NUMBER_TWELVE = 12
    const val DEFAULT_VALUE = 50
    /**
     * 是否显示或者隐藏fragment
     */
    const val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"
    /**
     * key of url
     */
    const val KEY_OF_URL = "url"

    const val SP_NAME = "eakay_sales_sp"

    /**
     * 保存用户TOKEN的key
     */
    const val KEY_AUTN_TOKEN = "key_auth_token"
    /**
     * 保存用户DEVICETOKEN的key
     */
    const val KEY_DEVICE_TOKEN = "key_device_token"
    /**
     * 保存用户是否登录的登录信息
     */
    const val KEY_IS_USER_LOGIN = "key_is_user_login"
    /**
     * 保存用户是否工作的信息
     */
    const val KEY_IS_USER_WORK = "key_is_user_work"
    /**
     * 自动填充账户名密码
     */
    const val KEY_AUTO_FILL_ACCOUNT = "key_auto_fill_account_in_signin"
    const val USERINFO_SP_NAME = "eakay_info"
    const val KEY_TEL_NUM = "telephone"
    const val KEY_USER_IDS = "user_id"
    const val KEY_USER_ACCOUNT = "user_account"

}