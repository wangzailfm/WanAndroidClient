package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.LoginResponse
import top.jowanxu.wanandroidclient.model.HomeModel
import top.jowanxu.wanandroidclient.model.HomeModelImpl
import top.jowanxu.wanandroidclient.view.MineFragmentView

class MineFragmentPresenterImpl(private val mineFragmentView: MineFragmentView) : HomePresenter.OnLoginListener, HomePresenter.OnRegisterListener {

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
        mineFragmentView.loginSuccess(result)
    }

    /**
     * login failed
     * @param errorMessage error message
     */
    override fun loginFailed(errorMessage: String?) {
        mineFragmentView.loginFailed(errorMessage)
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
        mineFragmentView.registerSuccess(result)
    }

    /**
     * register failed
     * @param errorMessage error message
     */
    override fun registerFailed(errorMessage: String?) {
        mineFragmentView.registerFailed(errorMessage)
    }
}