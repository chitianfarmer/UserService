package cn.eakay.service.base

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.eakay.service.R
import cn.eakay.service.network.SubscriptionManager
import cn.eakay.service.utils.RomUtils
import cn.eakay.service.utils.SystemBarUtils
import cn.eakay.service.utils.ToastUtils
import cn.eakay.service.utils.ViewUtils
import cn.eakay.service.widget.EToolbar
import java.util.ArrayList

/**
 * @packageName: UserService
 * @fileName: BaseActivity
 * @author: chitian
 * @date: 2019-07-09 15:25
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
abstract class BaseActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    private val INVALID_CONTENT_ID: Int = -1
    private var view: View? = null
    var mToolbar: EToolbar? = null

    /**
     * 退出程序.
     *
     * @param context
     */
    private var lastTimeStamp = 0L
    private val maxTimeStamp = 1350L
    /**
     * 申请请求的request code
     */
    private val PERMISSION_REQUEST = 1200
    /**
     * 是否跳转过应用程序信息详情页
     */
    private var mIsJump2Settings = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (RomUtils.isLightStatusBarAvailable()) {
            SystemBarUtils.StatusBarLightMode(this)
        }
        val layoutId = getLayoutId()
        if (layoutId != INVALID_CONTENT_ID) {
            view = View.inflate(this, layoutId, null)
            setContentView(view!!)
        } else {
            view = getContentView()
            if (getContentViewLayoutParams() != null) {
                setContentView(view!!, getContentViewLayoutParams())
            } else {
                setContentView(view!!)
            }
        }
        init(savedInstanceState)
        initToolbar()
        inflateView()
        onViewCreated()
    }

    open fun onViewCreated() {

    }

    open fun inflateView() {
    }

    open fun initToolbar() {
        val toolbarId = getToolbarId()
        if (toolbarId != View.NO_ID) {
            mToolbar = findViewById<EToolbar>(toolbarId)
            if (mToolbar != null) {
                mToolbar!!.setNavigationOnClickListener { v ->
                    if (finishOnClickNavigation()) {
                        finish()
                    }
                    onNavigationClick(v)
                }
            }
        }
    }

    open fun init(instanceState: Bundle?) {
        EakayApplication.instance!!.addActivity(this)
    }

    /**
     * the layout id that you will set content view.
     *
     * @return id
     */
    abstract fun getLayoutId(): Int

    private fun getContentView(): View {
        return view!!
    }

    private fun getContentViewLayoutParams(): ViewGroup.LayoutParams? {
        return view?.layoutParams
    }

    /**
     * get [cn.eakay.service.widget.EToolbar] Id
     *
     * @return id
     */
    open fun getToolbarId(): Int {
        return R.id.toolbar
    }

    /**
     * 点击 Toolbar 左边导航按钮 子类可以在此方法中处理事件
     *
     * @param v view
     */
    open fun onNavigationClick(v: View) {}

    /**
     * 是否希望 点击 Toolbar 左边导航按钮 完成finish功能，默认是true
     *
     * @return false 的话，子类可以处理自己的相关功能
     */
    open fun finishOnClickNavigation(): Boolean {
        return true
    }

    /**
     * Toolbar上右侧的那些 menu 按钮点击事件
     * 子类重写这个方法就处理响应事件
     *
     * @param item
     * @return
     */
    override fun onMenuItemClick(item: MenuItem): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        if (mIsJump2Settings) {
            onRecheckPermission()
            mIsJump2Settings = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SubscriptionManager.instance.cancelAll()
        EakayApplication.instance!!.removeActivity(this)

    }

    /**
     * 吐司工具类
     *
     * @param context
     * @param message
     */
    open fun showToast(context: Activity, message: String) {
        if (!isFinishing) {
            ToastUtils.showToast(context, message)
        }
    }

    open fun showProgress() {

    }

    open fun closeProgress() {

    }

    /**
     * 子类可以重写，点击空白处输入法框是否隐藏
     *
     * @return
     */
    open fun isHideSoftInput(): Boolean {
        return true
    }

    /**
     * 空白处回收软键盘
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (isHideSoftInput()) {
            try {
                if (ev.action == MotionEvent.ACTION_DOWN) {

                    // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
                    val v = currentFocus
                    if (isShouldHideInput(v, ev)) {
                        ViewUtils.closeKeyBoard(v)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            if (event.x > left && event.x < right
                && event.y > top && event.y < bottom
            ) {
                // 点击EditText的事件，忽略它。
                return false
            } else {
                // 点击其他地方清除焦点
                v.clearFocus()
                return true
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false
    }

    // ----------------------权限检测 不要插队----------------------

    /**
     * 单个权限的检查
     */
    fun checkPermission(@NonNull permission: String, @Nullable reason: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        val permissionCheck = ContextCompat.checkSelfPermission(this, permission)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //权限已经申请
            onPermissionGranted(permission)

        } else {
            if (!TextUtils.isEmpty(reason)) {
                //判断用户先前是否拒绝过该权限申请，如果为true，我们可以向用户解释为什么使用该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //这里的dialog可以自定义
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(reason)
                    builder.setPositiveButton(
                        getString(R.string.i_know)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        requestPermission(arrayOf(permission))
                    }

                    val mDialog = builder.create()
                    mDialog.setCancelable(false)
                    mDialog.show()
                } else {
                    requestPermission(arrayOf(permission))
                }
            } else {
                requestPermission(arrayOf(permission))
            }

        }
    }

    /**
     * 多个权限的检查
     */
    fun checkPermissions(@NonNull vararg permissions: String) {
        if (Build.VERSION.SDK_INT < 23) {
            return
        }
        //用于记录权限申请被拒绝的权限集合
        val permissionDeniedList = ArrayList<String>()
        for (permission in permissions) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, permission)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission)
            } else {
                permissionDeniedList.add(permission)
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            val deniedPermissions = permissionDeniedList.toTypedArray()
            requestPermission(deniedPermissions)
        }
    }

    /**
     * 调用系统API完成权限申请
     */
    open fun requestPermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST)
    }

    /**
     * 申请权限被允许的回调
     */
    open fun onPermissionGranted(permission: String) {

    }

    /**
     * 申请权限被拒绝的回调
     */
    open fun onPermissionDenied(permission: String) {

    }

    /**
     * 申请权限的失败的回调
     */
    open fun onPermissionFailure() {

    }

    /**
     * 如果从设置界面返回，则重新申请权限
     */
    open fun onRecheckPermission() {

    }

    /**
     * 弹出系统权限询问对话框，用户交互后的结果回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST -> if (grantResults.isNotEmpty()) {
                //用于记录是否有权限申请被拒绝的标记
                var isDenied = false
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        onPermissionGranted(permissions[i])
                    } else {
                        isDenied = true
                        onPermissionDenied(permissions[i])
                    }
                }
                if (isDenied) {
                    isDenied = false
                    //如果有权限申请被拒绝，则弹出对话框提示用户去修改权限设置。
                    showPermissionSettingsDialog(permissions, grantResults)
                }

            } else {
                onPermissionFailure()
            }
            else -> {
            }
        }
    }

    open fun showPermissionSettingsDialog(@NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.lack_of_necessary_permissions)
        builder.setNegativeButton(R.string.dialog_negative_button_text) { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton(R.string.go_to_set) { dialog, _ ->
            dialog.dismiss()
            jump2PermissionSettings()
        }

        val mDialog = builder.create()
        mDialog.setCancelable(false)
        mDialog.show()
    }

    /**
     * 跳转到应用程序信息详情页面
     */
    open fun jump2PermissionSettings() {
        mIsJump2Settings = true
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }
    // ----------------------权限检测 不要插队----------------------

    /**
     * 点击返回键2次退出程序
     *
     * @param context
     */
    open fun exitApplication(context: Activity) {
        val currentTimeStamp = System.currentTimeMillis()
        if (currentTimeStamp - lastTimeStamp > maxTimeStamp) {
            ToastUtils.showShort(R.string.press_again_to_exit)
        } else {
            EakayApplication.instance!!.finishAllActivity()
            finish()
        }
        lastTimeStamp = currentTimeStamp
    }
}