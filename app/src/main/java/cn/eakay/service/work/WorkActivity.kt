package cn.eakay.service.work

import cn.eakay.service.R
import cn.eakay.service.base.BaseActivity

/**
 * @packageName: UserService
 * @fileName: WorkActivity
 * @author: chitian
 * @date: 2019-07-18 15:50
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class WorkActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_base_no_title
    override fun onViewCreated() {
        super.onViewCreated()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_content, WorkFragment.newInstance)
        transaction.commitAllowingStateLoss()
    }
}