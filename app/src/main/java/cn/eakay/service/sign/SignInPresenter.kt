package cn.eakay.service.sign

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.main.MainActivity
import cn.eakay.service.network.ApiUtils
import cn.eakay.service.utils.ErrorManager
import cn.eakay.service.utils.SecurityUtils
import cn.eakay.service.utils.StringUtils
import com.alibaba.fastjson.JSONObject
import com.changyoubao.vipthree.base.LSPUtils
import com.shs.easywebviewsupport.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.apache.commons.logging.Log

/**
 * @packageName: UserService
 * @fileName: SignInPresenter
 * @author: chitian
 * @date: 2019-07-10 14:08
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SignInPresenter : SignInContract.Presenter {

    private var view: SignInContract.View? = null

    override fun submitToLogin() {
        val account = view!!.getInputAccount()
        val password = view!!.getInputPassword()
        view?.clearError()
        if (account.isEmpty()) {
            view?.toast(R.string.please_input_mobile)
            view?.setAccountError(R.string.please_input_mobile, true)
            return
        }
        if (!StringUtils.checkMobileNumber(account)) {
            view?.toast(R.string.phone_number_format_is_incorrect)
            view?.setAccountError(R.string.phone_number_format_is_incorrect, true)
            return
        }
        if (password.isEmpty()) {
            view?.toast(R.string.please_enter_your_password)
            view?.setPassWordError(R.string.please_enter_your_password, true)
            return
        }
        val deviceToken = LSPUtils.get(Constants.KEY_DEVICE_TOKEN, "")
        if (deviceToken.isEmpty()) {
            getDeviceToken(account, password)
        } else {
            login(account, password)
        }
    }


    override fun jumpToWorkView() {
        LSPUtils.put(Constants.KEY_IS_USER_WORK, true)
    }

    override fun attchView(view: SignInContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    @SuppressLint("CheckResult")
    private fun login(account: String, password: String) {
        var json = JSONObject()
        val deviceToken = LSPUtils.get(Constants.KEY_DEVICE_TOKEN, "")
        json["telphone"] = account
        json["password"] = password
        json["deviceToken"] = deviceToken
        val body = StringUtils.createBody(json)
        val signIn = ApiUtils.instance.service.signIn(body)
        signIn.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    when (result.getErrCode()) {
                        "0" -> {
                            val loginBean = result.getDatas()
                            val status = loginBean?.loginStatus
                            val statusComment = loginBean?.loginStatusComment
                            LogUtils.loge("登陆的状态：$status,注册的状态：$statusComment")
                            refreshLoginToken(account)
                        }
                        else -> {
                            val errCode = result.getErrCode()
                            val errMsg = result.getErrMsg()
                            LogUtils.loge("错误信息：$errMsg")
                            ErrorManager.checkResultError(errCode, errMsg)

                        }
                    }
                }, { error ->
                    val message = error.message
                    LogUtils.loge("登陆请求失败，错误信息：$message")
                    view?.toast("请求失败，错误信息：$message")
                })
    }

    @SuppressLint("CheckResult")
    private fun getDeviceToken(account: String, password: String) {
        var json = JSONObject()
        json["deviceToken"] = ""
        json["accessToken"] = ""
        val body = StringUtils.createBody(json)
        val deviceToken = ApiUtils.instance.service.getDeviceToken(body)
        deviceToken.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    when (result.getErrCode()) {
                        "0" -> {
                            val deviceBean = result.getDatas()
                            LSPUtils.put(Constants.KEY_DEVICE_TOKEN, deviceBean)
                            login(account, password)
                        }
                        else -> {
                            val errCode = result.getErrCode()
                            val errMsg = result.getErrMsg()
                            LogUtils.loge("请求错误错误信息：$errMsg")
                            ErrorManager.checkResultError(errCode, errMsg)
                        }
                    }
                }, { error ->
                    val message = error.message
                    LogUtils.loge("登陆获取deviceToken错误信息：$message")
                    view?.toast("请求失败，错误信息：$message")
                })
    }
    @SuppressLint("CheckResult")
    private fun refreshLoginToken(account: String){
        val activity = view?.getBaseActivity()
        val timeMillis = System.currentTimeMillis()
        val param = JSONObject()
        param["appId"] = Constants.APP_KEY
        param["secret"] = Constants.APP_SECRET
        param["timestamp"] = timeMillis
        val deviceToken = LSPUtils.get(Constants.KEY_DEVICE_TOKEN, "")
        if (deviceToken.isNotEmpty()) {
            param["deviceToken"] = deviceToken
        }
        param["sign"] = SecurityUtils.MD5(Constants.APP_KEY + Constants.APP_SECRET + (if (TextUtils.isEmpty(deviceToken)) "" else deviceToken) + timeMillis)
        val body = StringUtils.createBody(param)
        val authToken = ApiUtils.instance.service.refreshLoginAuthToken(body)
        authToken.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                val errCode = result.errCode
                val errMsg = result.errMsg
                val accessToken = result.accessToken
                when (errCode) {
                    "0" -> {
                        LSPUtils.put(Constants.KEY_AUTN_TOKEN, accessToken)
                        LSPUtils.put(Constants.KEY_IS_USER_LOGIN, true)
                        LSPUtils.put(Constants.KEY_AUTO_FILL_ACCOUNT,account)
                        val intent = Intent(activity, MainActivity::class.java)
                        activity?.startActivity(intent)
                        LSPUtils.put(Constants.KEY_IS_USER_LOGIN, true)
                        activity?.finish()
                        LogUtils.loge("刷新了登陆的Token：$accessToken")
                    }
                    else -> {
                        LogUtils.loge("请求失败，错误码：$errCode，错误信息：$errMsg")
                    }
                }
            }, { error ->
                val message = error.message
                LogUtils.loge("登陆刷新token请求失败：$message")
            })
    }
}