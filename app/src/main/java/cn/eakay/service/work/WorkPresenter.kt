package cn.eakay.service.work

import android.content.Intent
import android.text.TextUtils
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.WorkBean
import cn.eakay.service.main.MainActivity
import cn.eakay.service.network.ApiUtils
import cn.eakay.service.network.listener.ResultListener
import cn.eakay.service.network.listener.ResultObserver
import cn.eakay.service.utils.StringUtils
import com.alibaba.fastjson.JSONObject
import com.changyoubao.vipthree.base.LSPUtils
import com.shs.easywebviewsupport.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @packageName: UserService
 * @fileName: WorkPresenter
 * @author: chitian
 * @date: 2019-07-18 16:26
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class WorkPresenter : WorkContract.Presenter {
    private var view: WorkContract.View? = null
    override fun go2Work() {
        val activity = view?.getBaseActivity()
        val workerFirst = view?.getInputWorkerFirst()
        val workerSecond = view?.getInputWorkerSecond()
        if (workerFirst.isNullOrEmpty()) {
            view?.showError(activity!!.getString(R.string.please_enter_the_employee_number))
            view?.toast(R.string.please_enter_the_employee_number)
            return
        }
        view?.showLoadDialog()
        view?.hintErrorView()
        val params = JSONObject()
        params["firstUser"] = workerFirst
        params["secondUser"] = if (TextUtils.isEmpty(workerSecond)) "" else workerSecond
        val body = StringUtils.createBody(params)
        val observable = ApiUtils.instance.service.onLineWork(body)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(ResultObserver(object :
                ResultListener<WorkBean> {
                override fun success(result: WorkBean) {
                    view?.hintLoadDialog()
                    LSPUtils.put(Constants.KEY_IS_USER_WORK, true)
                    toMain()
                }

                override fun failed(error: Throwable?) {
                    view?.hintLoadDialog()
                    view?.hintErrorView()
                    val message = error?.message
//                    view?.toast("上班失败，错误信息：$message")
                    LogUtils.loge("上班失败错误信息：$message")
                }
            }))

    }

    override fun toMain() {
        val activity = view?.getBaseActivity()
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity?.startActivity(intent)
        activity?.finish()
    }

    override fun attachView(view: WorkContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }
}