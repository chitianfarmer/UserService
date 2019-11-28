package cn.eakay.service.second

import android.view.View
import androidx.fragment.app.FragmentTransaction
import cn.eakay.service.R
import cn.eakay.service.base.BaseActivity
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.messages.RefreshViewMessage
import org.greenrobot.eventbus.EventBus

/**
 * @packageName: UserService
 * @fileName: NeedSecondServiceActivity
 * @author: chitian
 * @date: 2019-11-27 09:03
 * @description: 需要二次服务
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class NeedSecondServiceActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_base_title
    override fun onViewCreated() {
        super.onViewCreated()
        mToolbar!!.setTitle(R.string.secondary_door_to_door_service_confirmation)
        val orderId = intent.getStringExtra(Constants.KEY_ORDER_ID)
        val fragment = NeedSecondServiceFragment.newInstance(orderId)
        val fragTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.replace(R.id.frame_content, fragment)
        fragTransaction.commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        EventBus.getDefault().post(RefreshViewMessage(Constants.NUMBER_ZERO))
        finish()
    }

    override fun onNavigationClick(v: View) {
        super.onNavigationClick(v)
        EventBus.getDefault().post(RefreshViewMessage(Constants.NUMBER_ZERO))
        finish()
    }
}