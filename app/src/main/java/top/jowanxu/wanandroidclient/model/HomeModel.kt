package top.jowanxu.wanandroidclient.model

import top.jowanxu.wanandroidclient.presenter.HomePresenter

/**
 * 首页Model接口
 */
interface HomeModel {
    /**
     * get Home List
     * @param onHomeListListener HomePresenter.OnHomeListListener
     * @param page page
     */
    fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page: Int = 0)

    /**
     * get TypeTree List
     * @param onTypeTreeListListener HomePresenter.OnTypeTreeListListener
     */
    fun getTypeTreeList(onTypeTreeListListener: HomePresenter.OnTypeTreeListListener)

    /**
     * login
     * @param onLoginListener HomePresenter.OnLoginListener
     * @param username username
     * @param password password
     */
    fun loginWanAndroid(onLoginListener: HomePresenter.OnLoginListener, username: String, password: String)

    /**
     * register
     * @param onRegisterListener HomePresenter.OnRegisterListener
     * @param username username
     * @param password password
     * @param repassword repassword
     */
    fun registerWanAndroid(onRegisterListener: HomePresenter.OnRegisterListener, username: String, password: String, repassword: String)

    /**
     * cancel HomeList Request
     */
    fun cancelHomeListRequest()
    /**
     * cancel TypeTree Request
     */
    fun cancelTypeTreeRequest()

    /**
     * cancel login Request
     */
    fun cancelLoginRequest()

    /**
     * cancel register Request
     */
    fun cancelRegisterRequest()
}