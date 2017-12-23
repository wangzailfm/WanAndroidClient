package top.jowanxu.wanandroidclient.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_mine.*
import top.jowanxu.wanandroidclient.R

class MineFragment : Fragment() {

    private var mainView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView = inflater.inflate(R.layout.fragment_mine, container, false)
        }
        return mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        login.setOnClickListener(onClickListener)
        register.setOnClickListener(onClickListener)
    }

    /**
     * OnClickListener
     */
    private val onClickListener = View.OnClickListener {
        view ->
        when (view.id) {
            R.id.login -> {
                if (checkContent(true)) {

                }
            }
            R.id.register -> {
                if (checkContent(false)) {

                }
            }
        }
    }

    /**
     * check username and password and password confirm field to login or register.
     * @param isLogin login is true, register is false.
     * @return success return true, failed return false.
     */
    private fun checkContent(isLogin: Boolean): Boolean {
        // error hint
        username.error = null
        password.error = null
        passwordConfirm.error = null
        // cancel
        var cancel = false
        // attempt to view
        var focusView: View? = null
        // if register, check password confirm
        val pwdConfirmText = passwordConfirm.text.toString()
        val pwdText = password.text.toString()
        val usernameText = username.text.toString()

        if (!isLogin) {
            if (TextUtils.isEmpty(pwdConfirmText)) {
                passwordConfirm.error = getString(R.string.password_confirm_not_empty)
                focusView = passwordConfirm
                cancel = true
            } else if (pwdConfirmText != pwdText) {
                passwordConfirm.error = getString(R.string.password_confirm_diff)
                focusView = passwordConfirm
                cancel = true
            }
        }
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
        } else {
            return true
        }
        return false
    }
}