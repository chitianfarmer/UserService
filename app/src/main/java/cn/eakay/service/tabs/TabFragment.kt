package cn.eakay.service.tabs

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import cn.eakay.service.R
import cn.eakay.service.adapter.TabAdapter
import cn.eakay.service.base.BaseFragment
import cn.eakay.service.base.Constants
import cn.eakay.service.utils.ToastUtils
import cn.eakay.service.utils.ViewUtils
import cn.eakay.service.widget.ItemDecoration
import com.alibaba.fastjson.JSONObject
import com.scwang.smartrefresh.header.WaterDropHeader
import com.scwang.smartrefresh.header.WaveSwipeHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.footer.FalsifyFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_tab.*

/**
 * @packageName: UserService
 * @fileName: TabFragment
 * @author: chitian
 * @date: 2019-07-12 10:14
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class TabFragment : BaseFragment(), TabContract.View, OnRefreshListener, OnLoadMoreListener, View.OnClickListener,TabAdapter.OnItemClickListener {

    private var pageNo: Int = Constants.PAGE_COUNT

    override fun getIntentType(): Int {
        return arguments!!.getInt(Constants.KEY_TYPE)
    }

    override fun showEmptyView() {

    }

    override fun showListView() {

    }

    override fun setLoadMoreEnable(enable: Boolean) {

    }

    override fun setRefreshEnable(enable: Boolean) {

    }

    override fun stopRefresh() {
    }

    override fun stopLoadMore() {
    }

    override fun updateListView(arrayList: List<JSONObject>) {
        adapter.notifyDataSetChanged()
    }

    override fun toast(msg: Any) {
        ToastUtils.showShort(msg)
    }

    override fun showLoadDialog() {
        showProgress()
    }

    override fun hintLoadDialog() {
        closeProgress()
    }

    override fun getBaseActivity(): Activity = activity!!

    private lateinit var adapter: TabAdapter
    private lateinit var presenter: TabPresenter

    companion object {
        fun newInstance(type: Int) = TabFragment()
            .apply { arguments = Bundle().apply { putInt(Constants.KEY_TYPE, type) } }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tab
    }

    override fun bindView() {
        presenter = TabPresenter()
        presenter.attchView(this@TabFragment)
    }

    override fun initData() {
        listView.layoutManager = GridLayoutManager(activity!!, Constants.NUMBER_ONE)
        adapter = TabAdapter(activity!!, presenter.getLists())
        listView.adapter = adapter
        presenter.requestData(pageNo)
        val dropHeader = WaterDropHeader(activity!!)
        refreshLayout.setRefreshHeader(dropHeader)
        val footer = ClassicsFooter(activity!!)
        refreshLayout.setRefreshFooter(footer)
        listView.addItemDecoration(ItemDecoration())
    }

    override fun setListener() {
        tv_request_again.setOnClickListener(this)
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setOnLoadMoreListener(this)
        adapter.setOnItemClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_request_again -> {
                if (ViewUtils.isFastClick()) {
                    return
                }
                onRefresh(refreshLayout)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNo = Constants.PAGE_COUNT
        presenter.requestData(pageNo)
        refreshLayout.finishRefresh(2000)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNo++
        presenter.requestData(pageNo)
        refreshLayout.finishLoadMore(2000)
    }
    override fun onClick(position: Int, bean: JSONObject) {
        presenter.onItemClick(position, bean)
    }

    override fun onLongClick(position: Int, bean: JSONObject) {
        presenter.onLongClick(position, bean)
    }

}