package cn.eakay.service.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.fragment.app.Fragment
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import cn.eakay.service.base.EakayApplication
import cn.eakay.service.beans.messages.LocationMessage
import cn.eakay.service.network.ApiUtils
import cn.eakay.service.network.listener.ResultListener
import cn.eakay.service.network.listener.ResultObserver
import cn.eakay.service.network.transformer.SchedulerProvider
import cn.eakay.service.tabs.TabFragment
import cn.eakay.service.utils.BdLocationHelper
import cn.eakay.service.utils.PermissionUtils
import cn.eakay.service.utils.StringUtils
import cn.eakay.service.widget.LoginDialog
import cn.eakay.service.work.WorkActivity
import com.alibaba.fastjson.JSONObject
import com.baidu.location.BDLocation
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.changyoubao.vipthree.base.LSPUtils
import com.shs.easywebviewsupport.utils.LogUtils
import java.util.ArrayList

/**
 * @packageName: UserService
 * @fileName: MainPresenter
 * @author: chitian
 * @date: 2019-07-09 16:43
 * @description: 主页面的presenter
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class MainPresenter : MainContract.Presenter {

    private var view: MainContract.View? = null
    private var fragments: ArrayList<Fragment>? = ArrayList()
    private var dataEntities: MutableList<JSONObject>? = ArrayList()

    override fun attachView(view: MainContract.View) {
        this.view = view
        startLocation()
    }

    override fun detachView() {
        stopLocation()
        view = null
    }

    override fun stopLocation() {
        BdLocationHelper.getInstance().stopLocation()
    }

    override fun startLocation() {
        val activity = view!!.getBaseActivity();
        if (!BdLocationHelper.getInstance().isLocationEnabled(activity)) {
            view!!.pleaseEnableYourLocationService()
            return
        }
        if (!PermissionUtils.hasPermissions(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            view!!.requestLocationPermission()
            return
        }
        BdLocationHelper.getInstance().startLocation()
    }

    override fun getRecordsList(): ArrayList<JSONObject> =
        ((if (dataEntities == null) ArrayList() else dataEntities) as ArrayList<JSONObject>)

    override fun getFragmentList(): ArrayList<Fragment> =
        (if (fragments == null) ArrayList() else fragments) as ArrayList<Fragment>

    override fun initFragments() {
        if (fragments!!.size != Constants.NUMBER_ZERO) {
            fragments!!.clear()
        }
        for (i in 0 until Constants.NUMBER_FIVE) {
            val fragment = TabFragment.newInstance(i)
            fragments!!.add(fragment)
        }
    }

    override fun toSignOutApp() {
        val activity = view!!.getBaseActivity()
        /*切换账户 弹出的确认提示框*/
        val builder = LoginDialog.Builder(activity)
        builder.setMessage(R.string.determined_to_work)
        builder.setGravity(LoginDialog.Builder.MESSAGE_CENTER_GRAVITY)
        builder.setNegativeButton(R.string.dialog_negative_button_text)
        builder.setPositiveButton(R.string.quit_app)
        builder.setOnDialogClickListener(object : LoginDialog.OnDialogClickListener {
            override fun onConfirmClick(dialog: Dialog?, which: Int) {
                dialog?.dismiss()
                /*点击退出后调用退出接口*/
                switchAccount()

            }

            override fun onCancelClick(dialog: Dialog?, which: Int) {
                dialog?.dismiss()
                view!!.resetUnLock()
            }

        })
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun initTabs() {
        val activity = view!!.getBaseActivity()
        if (dataEntities!!.size != Constants.NUMBER_ZERO) {
            dataEntities!!.clear()
        }
        for (i in Constants.NUMBER_ZERO until Constants.NUMBER_FIVE) {
            val entity = JSONObject()
            when (i) {
                Constants.NUMBER_ZERO -> {
                    entity[Constants.KEY_REFUND_REMARKS] =
                        activity.getString(R.string.pending_reception)
                    entity[Constants.KEY_TYPE] = R.drawable.pending_reception
                }
                Constants.NUMBER_ONE -> {
                    entity[Constants.KEY_REFUND_REMARKS] = activity.getString(R.string.pending)
                    entity[Constants.KEY_TYPE] = R.drawable.pending
                }
                Constants.NUMBER_TWO -> {
                    entity[Constants.KEY_REFUND_REMARKS] = activity.getString(R.string.to_be_paid)
                    entity[Constants.KEY_TYPE] = R.drawable.to_be_paid
                }
                Constants.NUMBER_THREE -> {
                    entity[Constants.KEY_REFUND_REMARKS] = activity.getString(R.string.cancelled)
                    entity[Constants.KEY_TYPE] = R.drawable.cancelled
                }
                Constants.NUMBER_FOUR -> {
                    entity[Constants.KEY_REFUND_REMARKS] = activity.getString(R.string.completed)
                    entity[Constants.KEY_TYPE] = R.drawable.completed
                }
            }
            dataEntities!!.add(entity)
        }
    }

    @SuppressLint("CheckResult")
    override fun switchAccount() {
        view?.showLoadDialog()
        val activity = view?.getBaseActivity()
        val params = JSONObject()
        val body = StringUtils.createBody(params)
        val observable = ApiUtils.instance.service.offLineWork(body)
        observable.compose(SchedulerProvider.instance.applySchedulers())
            .subscribe(ResultObserver(object :
                ResultListener<JSONObject> {
                override fun success(result: JSONObject) {
                    view?.hintLoadDialog()
                    LSPUtils.put(Constants.KEY_IS_USER_WORK, false)
                    EakayApplication.instance?.finishAllActivity()
                    val it = Intent(activity, WorkActivity::class.java)
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    activity?.startActivity(it)
                    activity?.finish()
                }

                override fun failed(error: Throwable?) {
                    view?.hintLoadDialog()
                    view?.resetUnLock()
                    val message = error?.message
                    view?.toast("下班失败，错误信息：$message")
                    LogUtils.loge("下班失败错误信息：$message")
                }
            }
            ))
    }

    override fun onLocationChanged(message: LocationMessage) {
        when (message.code) {
            Constants.NUMBER_ZERO -> {
                val location = message.bdLocation!!
                LogUtils.loge("----主页面定位成功：${location.addrStr}")

            }
            Constants.NUMBER_ONE -> {
                LogUtils.loge("----主页面定位失败")
            }
        }
    }
}