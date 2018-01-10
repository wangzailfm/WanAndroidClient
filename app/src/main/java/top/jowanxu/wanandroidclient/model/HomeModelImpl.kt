package top.jowanxu.wanandroidclient.model

import Constant
import RetrofitHelper
import cancelAndJoinByActive
import cancelByActive
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import top.jowanxu.wanandroidclient.bean.*
import top.jowanxu.wanandroidclient.presenter.HomePresenter
import tryCatch

class HomeModelImpl : HomeModel, CollectArticleModel {

    /**
     * Home list async
     */
    private var homeListAsync: Deferred<Any>? = null
    /**
     * TypeTree async
     */
    private var typeTreeListAsync: Deferred<Any>? = null
    /**
     * Hot async
     */
    private var hotListAsync: Deferred<Any>? = null
    /**
     * Login async
     */
    private var loginAsync: Deferred<Any>? = null
    /**
     * Register async
     */
    private var registerAsync: Deferred<Any>? = null
    /**
     * Friend list async
     */
    private var friendListAsync: Deferred<Any>? = null
    /**
     * Collect Article async
     */
    private var collectArticleAsync: Deferred<Any>? = null
    /**
     * get banner async
     */
    private var bannerAsync: Deferred<Any>? = null
    /**
     * Bookmark list async
     */
    private var bookmarkListAsync: Deferred<Any>? = null

    /**
     * get Home List
     * @param onHomeListListener HomePresenter.OnHomeListListener
     * @param page page
     */
    override fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page: Int) {
        async(UI) {
            try {
                homeListAsync?.cancelAndJoinByActive()
                homeListAsync = RetrofitHelper.retrofitService.getHomeList(page)
                // Get async result
                val result = homeListAsync?.await()
                if (result is HomeListResponse) {
                    onHomeListListener.getHomeListSuccess(result)
                } else {
                    onHomeListListener.getHomeListFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                // Return Throwable toString
                onHomeListListener.getHomeListFailed(t.toString())
                return@async
            }
        }
    }
    /**
     * cancel HomeList Request
     */
    override fun cancelHomeListRequest() {
        homeListAsync?.cancelByActive()
    }

    /**
     * get TypeTree List
     * @param onTypeTreeListListener HomePresenter.OnTypeTreeListListener
     */
    override fun getTypeTreeList(onTypeTreeListListener: HomePresenter.OnTypeTreeListListener) {
        async(UI) {
            try {
                typeTreeListAsync?.cancelAndJoinByActive()
                typeTreeListAsync = RetrofitHelper.retrofitService.getTypeTreeList()
                val result = typeTreeListAsync?.await()
                if (result is TreeListResponse) {
                    onTypeTreeListListener.getTypeTreeListSuccess(result)
                } else {
                    onTypeTreeListListener.getTypeTreeListFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onTypeTreeListListener.getTypeTreeListFailed(t.toString())
                return@async
            }
        }
    }
    /**
     * cancel TypeTree Request
     */
    override fun cancelTypeTreeRequest() {
        loginAsync?.cancelByActive()
    }

    /**
     * login
     * @param onLoginListener HomePresenter.OnLoginListener
     * @param username username
     * @param password password
     */
    override fun loginWanAndroid(onLoginListener: HomePresenter.OnLoginListener,
                                 username: String, password: String) {
        async(UI) {
            try {
                loginAsync?.cancelAndJoinByActive()
                loginAsync = RetrofitHelper.retrofitService.loginWanAndroid(username, password)
                // Get async result
                val result = loginAsync?.await()
                if (result is LoginResponse) {
                    onLoginListener.loginSuccess(result)
                } else {
                    onLoginListener.loginFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onLoginListener.loginFailed(t.toString())
                return@async
            }
        }
    }
    /**
     * cancel login Request
     */
    override fun cancelLoginRequest() {
        registerAsync?.cancelByActive()
    }

    /**
     * register
     * @param onRegisterListener HomePresenter.OnRegisterListener
     * @param username username
     * @param password password
     * @param repassword repassword
     */
    override fun registerWanAndroid(onRegisterListener: HomePresenter.OnRegisterListener,
                                    username: String, password: String, repassword: String) {
        async(UI) {
            try {
                registerAsync?.cancelAndJoinByActive()
                registerAsync = RetrofitHelper.retrofitService.registerWanAndroid(username, password, repassword)
                // Get async result
                val result = registerAsync?.await()
                if (result is LoginResponse) {
                    onRegisterListener.registerSuccess(result)
                } else {
                    onRegisterListener.registerFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onRegisterListener.registerFailed(t.toString())
                return@async
            }
        }
    }
    /**
     * cancel register Request
     */
    override fun cancelRegisterRequest() {
        typeTreeListAsync?.cancelByActive()
    }

    /**
     * get friend list
     */
    override fun getFriendList(onFriendListListener: HomePresenter.OnFriendListListener) {
        var throwable: Throwable? = null
        var bookmarkResult: FriendListResponse? = null
        var hotResult: HotKeyResponse? = null
        var commonResult: FriendListResponse? = null
        async(UI) {
            tryCatch({
                throwable = it
                it.printStackTrace()
            }) {
                bookmarkListAsync?.cancelAndJoinByActive()
                bookmarkListAsync = RetrofitHelper.retrofitService.getBookmarkList()
                val result = bookmarkListAsync?.await()
                if (result is FriendListResponse) {
                    bookmarkResult = result
                }
            }
            tryCatch({
                throwable = it
                it.printStackTrace()
            }) {
                hotListAsync?.cancelAndJoinByActive()
                hotListAsync = RetrofitHelper.retrofitService.getHotKeyList()
                val result = hotListAsync?.await()
                if (result is HotKeyResponse) {
                    hotResult = result
                }
            }
            tryCatch({
                throwable = it
                it.printStackTrace()
            }) {
                friendListAsync?.cancelAndJoinByActive()
                friendListAsync = RetrofitHelper.retrofitService.getFriendList()
                val result = friendListAsync?.await()
                if (result is FriendListResponse) {
                    commonResult = result
                }
            }
            throwable?.let {
                onFriendListListener.getFriendListFailed(it.toString())
                return@async
            }
            hotResult ?: let {
                onFriendListListener.getFriendListFailed(Constant.RESULT_NULL)
            }
            commonResult ?: let {
                onFriendListListener.getFriendListFailed(Constant.RESULT_NULL)
            }
            onFriendListListener.getFriendListSuccess(bookmarkResult, commonResult!!, hotResult!!)
        }
    }

    /**
     * cancel friend list Request
     */
    override fun cancelFriendRequest() {
        bookmarkListAsync?.cancelByActive()
        friendListAsync?.cancelByActive()
        hotListAsync?.cancelByActive()
    }

    /**
     * add or remove collect article
     */
    override fun collectArticle(onCollectArticleListener: HomePresenter.OnCollectArticleListener, id: Int, isAdd: Boolean) {
        async(UI) {
            try {
                collectArticleAsync?.cancelAndJoinByActive()
                collectArticleAsync = if (isAdd) {
                    RetrofitHelper.retrofitService.addCollectArticle(id)
                } else {
                    RetrofitHelper.retrofitService.removeCollectArticle(id)
                }
                val result = collectArticleAsync?.await()
                if (result is HomeListResponse) {
                    onCollectArticleListener.collectArticleSuccess(result, isAdd)
                } else {
                    onCollectArticleListener.collectArticleFailed(Constant.RESULT_NULL, isAdd)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onCollectArticleListener.collectArticleFailed(t.toString(), isAdd)
                return@async
            }
        }
    }

    /**
     * cancel collect article Request
     */
    override fun cancelCollectRequest() {
        collectArticleAsync?.cancelByActive()
    }

    /**
     * get banner
     * @param onBannerListener HomePresenter.OnBannerListener
     */
    override fun getBanner(onBannerListener: HomePresenter.OnBannerListener) {
        async(UI) {
            try {
                bannerAsync?.cancelAndJoinByActive()
                bannerAsync = RetrofitHelper.retrofitService.getBanner()
                val result = bannerAsync?.await()
                if (result is BannerResponse) {
                    onBannerListener.getBannerSuccess(result)
                } else {
                    onBannerListener.getBannerFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onBannerListener.getBannerFailed(t.toString())
                return@async
            }
        }
    }

    /**
     * cancel get banner request
     */
    override fun cancelBannerRequest() {
        bannerAsync?.cancelByActive()
    }
}