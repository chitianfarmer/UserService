package cn.eakay.service.tabs

import cn.eakay.service.base.BasePresenter
import cn.eakay.service.base.BaseView
import cn.eakay.service.beans.response.TabOrderListBean

/**
 * @packageName: UserService
 * @fileName: TabContract
 * @author: chitian
 * @date: 2019-07-12 11:32
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class TabContract {

    /**
     * the controller bind view
     */
    interface View : BaseView {

        /**
         * get intent type
         *
         * @return
         */
        fun getIntentType(): Int

        /**
         * show emptyView hint listView
         */
        fun showEmptyView()

        /**
         * show listView hint emptyView
         */
        fun showListView()

        /**
         * stop refreshing
         */
        fun stopRefresh()

        /**
         * stop loading more
         */
        fun stopLoadMore()

        /**
         * update list
         *
         * @param arrayList
         */
        fun updateListView(arrayList: List<TabOrderListBean.OrderBean>)
    }

    /**
     * the controller bind presenter
     */
    interface Presenter : BasePresenter<View> {

        /**
         * get data list
         *
         * @return
         */
        fun getLists(): List<TabOrderListBean.OrderBean>

        /**
         * request data from service
         *
         * @param page
         */
        fun requestData(page: Int)

        /**
         * on list item clicked
         *
         * @param position
         * @param object
         */
        fun onItemClick(position: Int, bean: TabOrderListBean.OrderBean)

        /**
         * on list item long click
         */
        fun onLongClick(position: Int, bean: TabOrderListBean.OrderBean)
    }
}