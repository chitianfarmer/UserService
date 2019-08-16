package cn.eakay.service.orders.rescue

import android.Manifest
import android.os.Bundle
import cn.eakay.service.R
import cn.eakay.service.base.BaseActivity
import cn.eakay.service.base.Constants
import cn.eakay.service.utils.PermissionUtils

/**
 * @packageName: UserService
 * @fileName: RescueOrderDetailActivity
 * @author: chitian
 * @date: 2019-07-20 09:22
 * @description: 救援订单详情
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class RescueOrderDetailActivity :BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_base_title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!PermissionUtils.hasPermissions(this, Manifest.permission.CALL_PHONE)) {
            checkPermission(Manifest.permission.CALL_PHONE, getString(R.string.request_call_phone_permission))
            return
        }
    }
    override fun onViewCreated() {
        super.onViewCreated()
        mToolbar?.setTitle(R.string.order_details)
        val orderType = intent.getStringExtra(Constants.KEY_ORDER_TYPE)
        val orderId = intent.getStringExtra(Constants.KEY_ORDER_ID)
        val fragment = RescueOrderDetailFragment.newInstance(orderType, orderId)
        val fragTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.replace(R.id.frame_content, fragment)
        fragTransaction.commitAllowingStateLoss()
    }
}