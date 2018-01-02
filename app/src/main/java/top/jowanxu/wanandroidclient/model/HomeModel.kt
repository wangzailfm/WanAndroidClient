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
     * cancel HomeList Request
     */
    fun cancelHomeListRequest()

    /**
     * get TypeTree List
     * @param onTypeTreeListListener HomePresenter.OnTypeTreeListListener
     */
    fun getTypeTreeList(onTypeTreeListListener: HomePresenter.OnTypeTreeListListener)
    /**
     * cancel TypeTree Request
     */
    fun cancelTypeTreeRequest()

    /**
     * login
     * @param onLoginListener HomePresenter.OnLoginListener
     * @param username username
     * @param password password
     */
    fun loginWanAndroid(onLoginListener: HomePresenter.OnLoginListener, username: String, password: String)
    /**
     * cancel login Request
     */
    fun cancelLoginRequest()

    /**
     * register
     * @param onRegisterListener HomePresenter.OnRegisterListener
     * @param username username
     * @param password password
     * @param repassword repassword
     */
    fun registerWanAndroid(onRegisterListener: HomePresenter.OnRegisterListener, username: String, password: String, repassword: String)

    /**
     * cancel register Request
     */
    fun cancelRegisterRequest()

    /**
     * get friend list
     * @param onFriendListListener HomePresenter.OnFriendListListener
     */
    fun getFriendList(onFriendListListener: HomePresenter.OnFriendListListener)

    /**
     * cancel friend list Request
     */
    fun cancelFriendRequest()

    /**
     * get banner
     * @param onBannerListener HomePresenter.OnBannerListener
     */
    fun getBanner(onBannerListener: HomePresenter.OnBannerListener)

    /**
     * cancel get banner request
     */
    fun cancelBannerRequest()

}