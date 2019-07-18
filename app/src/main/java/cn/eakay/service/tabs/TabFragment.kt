package cn.eakay.service.tabs

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.eakay.service.R
import cn.eakay.service.adapter.TabAdapter
import cn.eakay.service.base.BaseFragment
import cn.eakay.service.base.Constants
import cn.eakay.service.beans.TabOrderListBean
import cn.eakay.service.utils.ToastUtils
import cn.eakay.service.utils.ViewUtils
import cn.eakay.service.widget.ItemDecoration
import com.scwang.smartrefresh.header.WaterDropHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
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
class TabFragment : BaseFragment(), TabContract.View, OnRefreshListener, OnLoadMoreListener, View.OnClickListener,
    TabAdapter.OnItemClickListener {

    private var pageNo: Int = Constants.PAGE_COUNT

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
        presenter.attachView(this@TabFragment)
    }

    override fun initData() {
        listView.layoutManager = GridLayoutManager(activity!!, Constants.NUMBER_ONE) as RecyclerView.LayoutManager?
        adapter = TabAdapter(activity!!, presenter.getLists())
        listView.adapter = adapter
        val dropHeader = WaterDropHeader(activity!!)
        refreshLayout.setRefreshHeader(dropHeader)
        val footer = ClassicsFooter(activity!!)
        refreshLayout.setRefreshFooter(footer)
        listView.addItemDecoration(ItemDecoration())
        presenter.requestData(Constants.PAGE_COUNT)
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
                presenter.requestData(Constants.PAGE_COUNT)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNo = Constants.PAGE_COUNT
        presenter.requestData(pageNo)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNo++
        presenter.requestData(pageNo)
    }

    override fun onClick(position: Int, bean: TabOrderListBean.OrderBean) {
        presenter.onItemClick(position, bean)
    }

    override fun onLongClick(position: Int, bean: TabOrderListBean.OrderBean) {
        presenter.onLongClick(position, bean)
    }

    override fun getIntentType(): Int {
        return arguments!!.getInt(Constants.KEY_TYPE)
    }

    override fun showEmptyView() {
        refreshLayout.visibility = View.GONE
        rl_empty.visibility = View.VISIBLE
    }

    override fun showListView() {
        refreshLayout.visibility = View.VISIBLE
        rl_empty.visibility = View.GONE
    }

    override fun stopRefresh() {
        refreshLayout.finishRefresh()
    }

    override fun stopLoadMore() {
        refreshLayout.finishLoadMore()
    }

    override fun updateListView(arrayList: List<TabOrderListBean.OrderBean>) {
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
}