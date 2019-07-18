package cn.eakay.service.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import cn.eakay.service.R
import cn.eakay.service.base.BaseFragment
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.AuthTokenBean
import cn.eakay.service.main.MainActivity
import cn.eakay.service.network.ApiUtils
import cn.eakay.service.network.ResultListener
import cn.eakay.service.network.ResultObserver
import cn.eakay.service.sign.SignInActivity
import cn.eakay.service.utils.SecurityUtils
import cn.eakay.service.utils.StringUtils
import cn.eakay.service.work.WorkActivity
import com.alibaba.fastjson.JSONObject
import com.changyoubao.vipthree.base.LSPUtils
import com.shs.easywebviewsupport.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @packageName: UserService
 * @fileName: SplashFragment
 * @author: chitian
 * @date: 2019-07-11 16:28
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SplashFragment : BaseFragment() {

    private var warningText: String? = null

    companion object {
        val newInstance = SplashFragment()
    }

    private fun initAfterPermissionGranted() {
        /*
         * 重定向
         */
        redirect(false)
        /*
         * 查询Token
         */
        checkAuthToken()
    }

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun bindView() {
        if (TextUtils.isEmpty(warningText)) {
            warningText = getString(R.string.for_your_convenience) + getString(R.string.setting_path)
        }
//        if (!PermissionUtils.hasPermissions(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)
//            return
//        } else {
//            initAfterPermissionGranted()
//        }
        initAfterPermissionGranted()
    }

    override fun initData() {
    }

    override fun setListener() {

    }

    @SuppressLint("CheckResult")
    private fun checkAuthToken() {
        val token = LSPUtils.get(Constants.KEY_IS_USER_LOGIN, false)
        if (!token) {
            val timeMillis = System.currentTimeMillis()
            val param = JSONObject()
            param["appId"] = Constants.APP_KEY
            param["secret"] = Constants.APP_SECRET
            param["timestamp"] = timeMillis
            param["deviceToken"] = ""
            param["sign"] = SecurityUtils.MD5(Constants.APP_KEY + Constants.APP_SECRET + timeMillis)
            val body = StringUtils.createBody(param)
            val authToken = ApiUtils.instance.service.checkNoLoginAuthToken(body)
            authToken.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ResultObserver(
                    object : ResultListener<AuthTokenBean> {
                        override fun success(result: AuthTokenBean) {
                            val errCode = result.getErrCode()
                            val errMsg = result.getErrMsg()
                            val accessToken = result.getAccessToken()
                            when (errCode) {
                                "0" -> {
                                    LSPUtils.put(Constants.KEY_AUTN_TOKEN, accessToken)
                                }
                                else -> {
                                    LogUtils.loge("获取token请求失败，错误码：$errCode，错误信息：$errMsg")
                                }
                            }
                        }

                        override fun failed(error: Throwable?) {
                            val message = error?.message
                            LogUtils.loge("获取token请求失败：$message")
                        }
                    }
                ))
        }
    }

    /**
     * redirect
     *
     * @param rightNow
     */
    private fun redirect(rightNow: Boolean) {
        /*check user is sign in or no*/
        val userLogin = LSPUtils.get(Constants.KEY_IS_USER_LOGIN, false)
        /*check user is work or no*/
        val userWorked = LSPUtils.get(Constants.KEY_IS_USER_WORK, false)
        /* if no sign in ，to sign*/
        LogUtils.loge("---登陆：$userLogin---上班：$userWorked")
        Handler().postDelayed(Runnable {
            if (!userLogin) {
                /**登录 */
                goToSign()
            } else {
                /*if sign in ，check user is work or no ，if work go to main else to work */
                if (!userWorked) {
                    /**上班页面 */
                    goToWork()
                } else {
                    /**主页面 */
                    goToMain()
                }
            }
        }, (if (rightNow) 0 else 3000))
    }

    /**
     * 进入主界面
     *
     * @author pjc
     */
    private fun goToMain() {
        val intent = Intent(activity!!, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity!!.finish()
    }

    /**
     * 进入登录界面
     *
     * @author pjc
     */
    private fun goToSign() {
        val intent = Intent(activity!!, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity!!.finish()
    }

    /**
     * 进入登录界面
     *
     * @author pjc
     */
    private fun goToWork() {
        val intent = Intent(activity!!, WorkActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity!!.finish()
    }
}