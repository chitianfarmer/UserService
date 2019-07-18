package cn.eakay.service.main

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import cn.eakay.service.R
import cn.eakay.service.base.BaseFragment
import cn.eakay.service.base.Constants
import cn.eakay.service.utils.ToastUtils
import cn.eakay.service.widget.EakaySlideToUnLockView
import com.alibaba.fastjson.JSONObject
import com.shs.easywebviewsupport.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @packageName: UserService
 * @fileName: MainFragment
 * @author: chitian
 * @date: 2019-07-09 16:39
 * @description: 主页面的fragment
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class MainFragment : BaseFragment(), MainContract.View, EakaySlideToUnLockView.CallBack {


    private lateinit var presenter: MainPresenter
    private var pagerAdapter: MyPagerAdapter? = null

    companion object {
        val newInstance = MainFragment()
        var pageCount = Constants.PAGE_COUNT
    }

    override fun bindView() {
        presenter = MainPresenter()
        presenter.apply {
            presenter.attachView(this@MainFragment)
        }

    }

    override fun initData() {
        presenter.initTabs()
        presenter.initFragments()
        pagerAdapter = MyPagerAdapter(childFragmentManager, presenter.getRecordsList(), activity!!)
        content_layout.adapter = pagerAdapter
        tab_layout.setupWithViewPager(content_layout)
        showTabs(presenter.getRecordsList())
    }

    override fun setListener() {
        slide_view.setmCallBack(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun toast(msg: Any) {
        ToastUtils.showShort(msg)
    }

    override fun showLoadDialog() {
        showProgress()
    }

    override fun hintLoadDialog() {
        closeProgress()
    }

    override fun getBaseActivity(): Activity {
        return activity!!
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onSlide(distance: Int) {
        /*滑动的距离*/
        LogUtils.loge("----滑动距离：$distance")
    }

    override fun onUnlocked() {
        /*下班*/
        presenter.toSignOutApp()
    }

    override fun showTabs(tabs: List<JSONObject>) {
        tab_layout.removeAllTabs()
        //添加前全部清空Tab
        for (i in tabs.indices) {
            val numalTab = tab_layout.newTab()
            numalTab.customView = pagerAdapter?.getTabView(i)
            tab_layout.addTab(numalTab)
        }
    }

    override fun initFragments(fragments: List<Fragment>) {
        if (pagerAdapter != null) {
            pagerAdapter!!.notifyDataSetChanged()
        }
    }

    override fun pleaseEnableYourLocationService() {
//判断位置服务有没有开启
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage(R.string.open_location)
        builder.setPositiveButton(
            getString(R.string.to_open)
        ) { dialog, _ ->
            dialog.dismiss()
//            jump2PermissionSettings()
        }
        builder.setNegativeButton(
            R.string.dialog_negative_button_text
        ) { dialog, _ -> dialog.dismiss() }
        val mDialog = builder.create()
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.setCancelable(false)
        mDialog.show()
    }

    override fun requestLocationPermission() {
//        val locationPermissions =
//            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
//        requestPermission(locationPermissions)
    }

    override fun resetUnLock() {
        slide_view.resetView()
    }

    /**
     * Viewpager适配器
     */
    private inner class MyPagerAdapter(fm: FragmentManager, val recordsList: List<JSONObject>, val mContext: Context) :
        FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return presenter.getFragmentList().size
        }

        override fun getItem(position: Int): Fragment {
            return presenter.getFragmentList()[position]
        }

        override fun getPageTitle(position: Int): CharSequence {
            val titleBean = recordsList[position]
            return titleBean.getString(Constants.KEY_REFUND_REMARKS)
        }

        fun getTabView(position: Int): View {
            val bean = recordsList[position]
            val title = bean.getString(Constants.KEY_REFUND_REMARKS)
            val tabView = View.inflate(mContext, R.layout.item_tab, null)
            val ivTabIcon = tabView.findViewById<ImageView>(R.id.iv_tab_icon)
            val tvTabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)
            when (position) {
                Constants.NUMBER_ZERO -> ivTabIcon.setImageResource(R.drawable.pending_reception)
                Constants.NUMBER_ONE -> ivTabIcon.setImageResource(R.drawable.pending)
                Constants.NUMBER_TWO -> ivTabIcon.setImageResource(R.drawable.to_be_paid)
                Constants.NUMBER_THREE -> ivTabIcon.setImageResource(R.drawable.cancelled)
                Constants.NUMBER_FOUR -> ivTabIcon.setImageResource(R.drawable.completed)
            }
            tvTabTitle.text = title
            return tabView
        }
    }
}