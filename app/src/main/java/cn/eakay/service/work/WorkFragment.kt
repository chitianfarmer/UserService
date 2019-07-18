package cn.eakay.service.work

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import cn.eakay.service.R
import cn.eakay.service.base.BaseFragment
import cn.eakay.service.base.Constants
import cn.eakay.service.utils.ToastUtils
import cn.eakay.service.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_go_to_work.*

/**
 * @packageName: UserService
 * @fileName: WorkFragment
 * @author: chitian
 * @date: 2019-07-18 16:02
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class WorkFragment : BaseFragment(), WorkContract.View, View.OnClickListener, TextWatcher {
    private lateinit var presenter: WorkPresenter

    override fun getLayoutId(): Int = R.layout.fragment_go_to_work

    companion object {
        val newInstance = WorkFragment()
    }

    override fun bindView() {
        presenter = WorkPresenter()
        presenter.attachView(this)
    }

    override fun initData() {
    }

    override fun setListener() {
        tv_go_to_work.setOnClickListener(this)
        edt_job_number_one.addTextChangedListener(this)
    }

    override fun getInputWorkerFirst(): String? {
        return edt_job_number_one.text.toString().trim()
    }

    override fun getInputWorkerSecond(): String? {
        return edt_job_number_two.text.toString().trim()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_go_to_work -> {
                if (ViewUtils.isFastClick()) {
                    return
                }
                presenter.go2Work()
            }
        }
    }

    override fun showError(error: String) {
        tv_error_tips.visibility = View.VISIBLE
        tv_error_tips.text = error
    }

    override fun hintErrorView() {
        tv_error_tips.visibility = View.INVISIBLE
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

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val numberOne = getInputWorkerFirst()?.length
        if (numberOne != null) {
            tv_go_to_work.isEnabled = (numberOne > Constants.NUMBER_ZERO)
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}