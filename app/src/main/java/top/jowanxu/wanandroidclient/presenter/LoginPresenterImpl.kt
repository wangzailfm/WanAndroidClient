package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.LoginResponse
import top.jowanxu.wanandroidclient.model.HomeModel
import top.jowanxu.wanandroidclient.model.HomeModelImpl
import top.jowanxu.wanandroidclient.view.LoginView

class LoginPresenterImpl(private val loginView: LoginView) : HomePresenter.OnLoginListener, HomePresenter.OnRegisterListener {

    private val homeModel: HomeModel = HomeModelImpl()
    /**
     * login
     * @param username username
     * @param password password
     */
    override fun loginWanAndroid(username: String, password: String) {
        homeModel.loginWanAndroid(this, username, password)
    }

    /**
     * login success
     * @param result LoginResponse
     */
    override fun loginSuccess(result: LoginResponse) {
        if (result.errorCode != 0) {
            loginView.loginFailed(result.errorMsg)
        } else {
            loginView.loginSuccess(result)
            loginView.loginRegisterAfter(result)
        }
    }

    /**
     * login failed
     * @param errorMessage error message
     */
    override fun loginFailed(errorMessage: String?) {
        loginView.loginFailed(errorMessage)
    }

    /**
     * register
     * @param username username
     * @param password password
     * @param repassword repassword
     */
    override fun registerWanAndroid(username: String, password: String, repassword: String) {
        homeModel.registerWanAndroid(this, username, password, repassword)
    }

    /**
     * register success
     * @param result LoginResponse
     */
    override fun registerSuccess(result: LoginResponse) {
        if (result.errorCode != 0) {
            loginView.registerFailed(result.errorMsg)
        } else {
            loginView.registerSuccess(result)
            loginView.loginRegisterAfter(result)
        }
    }

    /**
     * register failed
     * @param errorMessage error message
     */
    override fun registerFailed(errorMessage: String?) {
        loginView.registerFailed(errorMessage)
    }
}