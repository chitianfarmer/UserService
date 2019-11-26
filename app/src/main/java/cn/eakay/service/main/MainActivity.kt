package cn.eakay.service.main

import android.Manifest
import android.app.Activity
import android.os.Bundle
import cn.eakay.service.R
import cn.eakay.service.base.BaseActivity
import cn.eakay.service.utils.PermissionUtils

/**
 * @packageName: UserService
 * @fileName: MainActivity
 * @author: chitian
 * @date: 2019-07-09 15:56
 * @description: 主页面
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_base_no_title

    override fun onViewCreated() {
        initView()
        initData()
        setListener()
    }


    private fun initView() {
        val transaction = supportFragmentManager.beginTransaction();
        transaction.add(R.id.frame_content, MainFragment.newInstance)
        transaction.commitAllowingStateLoss()
    }

    private fun initData() {
    }

    private fun setListener() {
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!PermissionUtils.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getString(R.string.please_open_location_permission))
            return
        }
    }

    override fun exitApplication(context: Activity) {
        super.exitApplication(context)
    }
}