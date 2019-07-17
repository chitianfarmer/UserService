package cn.eakay.service.sign

import android.annotation.SuppressLint
import android.content.Intent
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.main.MainActivity
import cn.eakay.service.network.ApiUtils
import cn.eakay.service.utils.StringUtils
import com.alibaba.fastjson.JSONObject
import com.changyoubao.vipthree.base.LSPUtils
import com.shs.easywebviewsupport.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

    @SuppressLint("CheckResult")
    override fun submitToLogin() {
        val activity = view?.getBaseActivity()
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
                        }
                        else -> {
                            val errCode = result.getErrCode()
                            val errMsg = result.getErrMsg()
                            LogUtils.loge("错误信息：$errMsg")
                            view?.toast("请求失败，错误码：$errCode")
                        }
                    }
                }, { error ->
                    val message = error.message
                    LogUtils.loge("错误信息：$message")
                })
        view?.toast("输入的账号是：$account,输入的密码是：$password")
        val intent = Intent(activity, MainActivity::class.java)
        activity?.startActivity(intent)
        LSPUtils.put(Constants.KEY_IS_USER_LOGIN, true)
        activity?.finish()
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
}