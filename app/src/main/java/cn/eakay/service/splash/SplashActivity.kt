package cn.eakay.service.splash

import cn.eakay.service.R
import cn.eakay.service.base.BaseActivity

/**
 * @packageName: UserService
 * @fileName: SplashActivity
 * @author: chitian
 * @date: 2019-07-11 16:17
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SplashActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_base_no_title
    override fun onViewCreated() {
        super.onViewCreated()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_content, SplashFragment.newInstance)
        transaction.commitAllowingStateLoss()
    }
}