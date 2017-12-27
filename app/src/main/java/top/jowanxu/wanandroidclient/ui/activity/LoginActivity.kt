package top.jowanxu.wanandroidclient.ui.activity

import Constant
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.gyf.barlibrary.ImmersionBar
import kotlinx.android.synthetic.main.activity_login.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.base.BaseActivity
import top.jowanxu.wanandroidclient.base.Preference
import top.jowanxu.wanandroidclient.bean.LoginResponse
import top.jowanxu.wanandroidclient.presenter.LoginPresenterImpl
import top.jowanxu.wanandroidclient.view.LoginView

class LoginActivity : BaseActivity(), LoginView {
    /**
     * check login for SharedPreferences
     */
    private var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)
    /**
     * local username
     */
    private var user: String by Preference(Constant.USERNAME_KEY, "")
    /**
     * local password
     */
    private var pwd: String by Preference(Constant.PASSWORD_KEY, "")

    private val loginPresenter: LoginPresenterImpl by lazy {
        LoginPresenterImpl(this)
    }

    override fun setLayoutId(): Int = R.layout.activity_login

    override fun initImmersionBar() {
        super.initImmersionBar()
        if (ImmersionBar.isSupportStatusBarDarkFont())
            immersionBar.statusBarDarkFont(true).init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login.setOnClickListener(onClickListener)
        register.setOnClickListener(onClickListener)
        loginExit.setOnClickListener(onClickListener)
    }

    /**
     * login or register success after operation
     * @param result LoginResponse
     */
    override fun loginRegisterAfter(result: LoginResponse) {
        isLogin = true
        user = result.data.username
        pwd = result.data.password
        loginProgress.visibility = View.GONE
        setResult(Activity.RESULT_OK, Intent().apply { putExtra(Constant.CONTENT_TITLE_KEY, result.data.username) })
        finish()
    }

    /**
     * login success
     * @param result LoginResponse
     */
    override fun loginSuccess(result: LoginResponse) {
        toast(getString(R.string.login_success))
    }

    /**
     * login failed
     * @param errorMessage error message
     */
    override fun loginFailed(errorMessage: String?) {
        isLogin = false
        loginProgress.visibility = View.GONE
        errorMessage?.let {
            toast(it)
        }
    }

    /**
     * register success
     * @param result LoginResponse
     */
    override fun registerSuccess(result: LoginResponse) {
        toast(getString(R.string.register_success))
    }

    /**
     * register failed
     * @param errorMessage error message
     */
    override fun registerFailed(errorMessage: String?) {
        isLogin = false
        loginProgress.visibility = View.GONE
        username.requestFocus()
        errorMessage?.let {
            toast(it)
        }
    }

    /**
     * OnClickListener
     */
    private val onClickListener = View.OnClickListener {
        view ->
        when (view.id) {
            R.id.login -> {
                if (checkContent()) {
                    loginProgress.visibility = View.VISIBLE
                    loginPresenter.loginWanAndroid(username.text.toString(), password.text.toString())
                }
            }
            R.id.register -> {
                if (checkContent()) {
                    loginProgress.visibility = View.VISIBLE
                    loginPresenter.registerWanAndroid(username.text.toString(),
                            password.text.toString(), password.text.toString())
                }
            }
            R.id.loginExit -> {
                finish()
            }
        }
    }

    /**
     * check username and password and password confirm field to login or register.
     * @return success return true, failed return false.
     */
    private fun checkContent(): Boolean {
        // error hint
        username.error = null
        password.error = null
        // cancel
        var cancel = false
        // attempt to view
        var focusView: View? = null
        // if register, check password confirm
        val pwdText = password.text.toString()
        val usernameText = username.text.toString()

        // check password
        if (TextUtils.isEmpty(pwdText)) {
            password.error = getString(R.string.password_not_empty)
            focusView = password
            cancel = true
        } else if (password.text.length < 6) {
            password.error = getString(R.string.password_length_short)
            focusView = password
            cancel = true
        }

        // check username
        if (TextUtils.isEmpty(usernameText)) {
            username.error = getString(R.string.username_not_empty)
            focusView = username
            cancel = true
        }

        // requestFocus
        if (cancel) {
            if (focusView != null) {
                focusView.requestFocus()
            }
            return false
        } else {
            return true
        }
    }
}