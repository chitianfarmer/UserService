package cn.eakay.service.utils

import cn.eakay.service.BuildConfig
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.base.EakayApplication

/**
 * @packageName: UserService
 * @fileName: ErrorManager
 * @author: chitian
 * @date: 2019-07-11 08:23
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
object ErrorManager {
    /**
     * 错误码状态 1201040005 其他地方登陆  1201040006 无效的账户
     * 120160001 验证码失效 1201040001 Token失效   1201050001  无效的API地址  1201050002 无效的APP
     * 0 成功 其余均为出错 1201020001 无效的邀请码  -1 数据转换错误 -2 数据为空 1201010003 身份证格式错误
     * 1201060020 下单验证码错误 1201060009 更新银行卡时间不到  1101060047 账号信息有无
     */
    fun checkResultError(errorCode: String?, errorMessage: String?): String? {
        var msg: String? = null
        when (errorCode) {
            Constants.KEY_REQUEST_USER_NOT_EXIT_CODE
            -> {
                ToastUtils.showShort(R.string.invalid_account)
                msg = EakayApplication.instance!!.getString(R.string.invalid_account)
            }
            Constants.KEY_REQUEST_WRONG_VERIFY_CODE
            -> {
                ToastUtils.showShort(R.string.invalidVerificationCode)
                msg = EakayApplication.instance!!.getString(R.string.invalidVerificationCode)
            }
            Constants.KEY_REQUEST_API_CODE, Constants.KEY_REQUEST_APP_CODE
            -> {
                ToastUtils.showShort(R.string.system_denied_access)
                msg = EakayApplication.instance!!.getString(R.string.system_denied_access)
            }
            Constants.KEY_REQUEST_INVITED_CODE
            -> {
                ToastUtils.showShort(R.string.the_submitted_invitation_code_does_not_exist)
                msg = EakayApplication.instance!!.getString(R.string.the_submitted_invitation_code_does_not_exist)
            }
            Constants.KEY_REQUEST_418_CODE, Constants.KEY_REQUEST_DATAS_EMPTY_CODE, Constants.KEY_REQUEST_DATAS_CHANGE_CODE
            -> {
                ToastUtils.showShort(R.string.request_failed)
                msg = EakayApplication.instance!!.getString(R.string.request_failed)
            }
            Constants.KEY_REQUEST_500_CODE
            -> {
                ToastUtils.showShort(R.string.server_exception)
                msg = EakayApplication.instance!!.getString(R.string.server_exception)
            }
            Constants.KEY_REQUEST_501_CODE, Constants.KEY_REQUEST_502_CODE
            -> {
                ToastUtils.showShort(R.string.access_to_the_server_failed_again)
                msg = EakayApplication.instance!!.getString(R.string.access_to_the_server_failed_again)
            }
            Constants.KEY_REQUEST_ERROR_IDCARD_CODE
            -> {
                ToastUtils.showShort(R.string.id_card_format_is_incorrect)
                msg = EakayApplication.instance!!.getString(R.string.id_card_format_is_incorrect)
            }
            Constants.KEY_REQUEST_ORDER_INVITED_CODE
            -> {
                ToastUtils.showShort(R.string.verification_code_error)
                msg = EakayApplication.instance!!.getString(R.string.verification_code_error)
            }
            Constants.KEY_USER_ACOUNT_WORNG_CODE,Constants.KEY_USER_ACOUN_TNAME_WORNG_CODE
            -> {
                ToastUtils.showShort(R.string.incorrect_employee_information)
                msg = EakayApplication.instance!!.getString(R.string.incorrect_employee_information)
            }
            else
            -> {
                when (BuildConfig.DEBUG) {
                    errorCode.isNullOrEmpty()
                    -> {
                        ToastUtils.showShort(R.string.request_failed)
                        msg = EakayApplication.instance!!.getString(R.string.request_failed)
                    }
                    else
                    -> {
                        if (BuildConfig.DEBUG) {
                            ToastUtils.showShort(
                                String.format(
                                    EakayApplication.instance!!.getString(R.string.request_failed_has_error_code_msg),
                                    errorCode,
                                    errorMessage
                                )
                            )
                            msg = String.format(
                                EakayApplication.instance!!.getString(R.string.request_failed_has_error_code_msg),
                                errorCode,
                                errorMessage
                            )
                        } else {
                            ToastUtils.showShort(
                                String.format(
                                    EakayApplication.instance!!.getString(R.string.request_failed_has_error_code),
                                    errorCode
                                )
                            )
                            msg = String.format(
                                EakayApplication.instance!!.getString(R.string.request_failed_has_error_code),
                                errorCode
                            )
                        }
                    }
                }
            }
        }
        return msg
    }
}