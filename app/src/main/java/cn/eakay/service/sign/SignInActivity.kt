package cn.eakay.service.sign

import android.Manifest
import android.os.Bundle
import cn.eakay.service.R
import cn.eakay.service.base.BaseActivity
import cn.eakay.service.utils.PermissionUtils

/**
 * @packageName: UserService
 * @fileName: SignInActivity
 * @author: chitian
 * @date: 2019-07-10 08:53
 * @description: 登陆页面
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SignInActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_base_no_title
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!PermissionUtils.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getString(R.string.please_open_location_permission))
            return
        }
    }

    override fun onViewCreated() {
        super.onViewCreated()

        val transaction = supportFragmentManager.beginTransaction();
        transaction.add(R.id.frame_content, SignInFragment.newInstance)
        transaction.commitAllowingStateLoss()
    }
}