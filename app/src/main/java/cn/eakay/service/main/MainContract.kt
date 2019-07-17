package cn.eakay.service.main

import androidx.fragment.app.Fragment
import cn.eakay.service.base.BasePresenter
import cn.eakay.service.base.BaseView
import com.alibaba.fastjson.JSONObject
import java.util.ArrayList

/**
 * @packageName: UserService
 * @fileName: MainContract
 * @author: chitian
 * @date: 2019-07-09 16:41
 * @description: 主控制器
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class MainContract {

    interface View : BaseView {
        /**
         * show tabs
         *
         * @param tabs
         */
         fun showTabs(tabs: List<JSONObject>)

        /**
         * 初始化Fragments
         *
         * @param fragments
         */
         fun initFragments(fragments: List<Fragment>)

        /**
         * show pop up window with open location permission
         */
         fun pleaseEnableYourLocationService()

        /**
         * request location permission
         */
         fun requestLocationPermission()
        /**
         * reset unlockView
         */
         fun resetUnLock()
    }

    interface Presenter : BasePresenter<View> {
        /**
         * start location
         */
        fun startLocation()

        /**
         * stop location
         */
        fun stopLocation()

        /**
         * get tab item info
         *
         * @return
         */
        fun getRecordsList(): ArrayList<JSONObject>

        /**
         * get fragments
         *
         * @return
         */
        fun getFragmentList(): ArrayList<Fragment>

        /**
         * init fragments
         */
        fun initFragments()

        /**
         * to sign out app
         */
        fun toSignOutApp()

        /**
         * init tabs
         */
        fun initTabs()

        /**
         * exit app
         */
        fun switchAccount()
    }
}