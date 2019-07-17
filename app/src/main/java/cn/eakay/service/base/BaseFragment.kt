package cn.eakay.service.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @packageName: UserService
 * @fileName: BaseFragment
 * @author: chitian
 * @date: 2019-07-09 16:37
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
abstract class BaseFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindView()
        initData()
        setListener()
    }

    abstract fun getLayoutId(): Int
    abstract fun bindView()
    abstract fun initData()
    abstract fun setListener()

    fun showProgress() {

    }

    fun closeProgress() {

    }
}