package cn.eakay.service.sign

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import cn.eakay.service.R
import cn.eakay.service.base.BaseFragment
import cn.eakay.service.base.Constants
import cn.eakay.service.utils.ToastUtils
import cn.eakay.service.utils.ViewUtils
import com.changyoubao.vipthree.base.LSPUtils
import kotlinx.android.synthetic.main.fragment_sign_in.*

/**
 * @packageName: UserService
 * @fileName: SignInFragment
 * @author: chitian
 * @date: 2019-07-10 13:56
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class SignInFragment : BaseFragment(), SignInContract.View, View.OnClickListener, TextWatcher {


    private lateinit var presenter: SignInPresenter

    override fun getInputAccount(): String {
        return edt_mobile.text.toString().trim()
    }

    override fun getInputPassword(): String {
        return edt_password.text.toString().trim()
    }

    override fun toast(msg: Any) {
        ToastUtils.showShort(msg)
    }

    override fun showLoadDialog() {
    }

    override fun hintLoadDialog() {
    }

    override fun getBaseActivity(): Activity {
        return activity!!
    }

    override fun getLayoutId(): Int = R.layout.fragment_sign_in

    companion object {
        val newInstance = SignInFragment()
    }


    override fun bindView() {
        presenter = SignInPresenter()
        presenter.apply {
            presenter.attachView(this@SignInFragment)
        }
    }

    override fun initData() {
        rl_input.hint = getString(R.string.please_input_mobile)
        rl_password.hint = getString(R.string.please_enter_your_password)
        val account = LSPUtils.get(Constants.KEY_AUTO_FILL_ACCOUNT, "")
        if (account.isNotEmpty()) {
            edt_mobile.setText(account)
            edt_mobile.setSelection(edt_mobile.text.toString().length)
        }
    }

    override fun setListener() {
        tv_sign_in.setOnClickListener(this)
        edt_mobile.addTextChangedListener(this)
        edt_password.addTextChangedListener(this)

    }

    override fun setPassWordError(msg: Any?, isError: Boolean) {
        when (msg) {
            is String -> rl_password.error = msg
            is Int -> rl_password.error = getString(msg)
        }
        rl_password.isErrorEnabled = isError

    }

    override fun setAccountError(msg: Any?, isError: Boolean) {
        when (msg) {
            is String -> rl_input.error = msg
            is Int -> rl_input.error = getString(msg)
        }
        rl_input.isErrorEnabled = isError
    }

    override fun clearError() {
        rl_input.isErrorEnabled = false
        rl_password.isErrorEnabled = false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_sign_in -> {
                if (tv_error.visibility == View.VISIBLE) {
                    tv_error.visibility = View.INVISIBLE
                }
                if (ViewUtils.isFastClick()) {
                    return
                }
                presenter.submitToLogin()
            }

        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val phone = getInputAccount().length
        val passWord = getInputPassword().length
        tv_sign_in.isEnabled = phone > 0 && passWord > 0
    }

}