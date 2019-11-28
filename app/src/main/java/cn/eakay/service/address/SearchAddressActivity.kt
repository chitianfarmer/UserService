package cn.eakay.service.address

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import cn.eakay.service.R
import cn.eakay.service.base.BaseActivity
import cn.eakay.service.utils.PermissionUtils.hasPermissions

/**
 * @packageName: UserService
 * @fileName: SearchAddressActivity
 * @author: chitian
 * @date: 2019-11-26 15:56
 * @description: 搜索地址
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SearchAddressActivity : BaseActivity() {
    override fun getLayoutId(): Int {
       return R.layout.activity_base_title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getString(R.string.please_open_location_permission))
            return
        }
    }
    override fun onViewCreated() {
        super.onViewCreated()
        mToolbar!!.setTitle(R.string.confirm_secondary_service_address)
        val fragment: SearchAddressFragment = SearchAddressFragment.newInstance()
        val fragTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.replace(R.id.frame_content, fragment)
        fragTransaction.commitAllowingStateLoss()
    }
}