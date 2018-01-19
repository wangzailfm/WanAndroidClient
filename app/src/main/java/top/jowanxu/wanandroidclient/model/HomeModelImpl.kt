package top.jowanxu.wanandroidclient.model

import Constant
import RetrofitHelper
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
    private var homeListAsync: Deferred<HomeListResponse>? = null
    /**
     * TypeTree async
     */
    private var typeTreeListAsync: Deferred<TreeListResponse>? = null
    /**
     * Hot async
     */
    private var hotListAsync: Deferred<HotKeyResponse>? = null
    /**
     * Login async
     */
    private var loginAsync: Deferred<LoginResponse>? = null
    /**
     * Register async
     */
    private var registerAsync: Deferred<LoginResponse>? = null
    /**
     * Friend list async
     */
    private var friendListAsync: Deferred<FriendListResponse>? = null
    /**
     * Collect Article async
     */
    private var collectArticleAsync: Deferred<HomeListResponse>? = null
    /**
     * get banner async
     */
    private var bannerAsync: Deferred<BannerResponse>? = null
    /**
     * Bookmark list async
     */
    private var bookmarkListAsync: Deferred<FriendListResponse>? = null

    /**
     * get Home List
     * @param onHomeListListener HomePresenter.OnHomeListListener
     * @param page page
     */
    override fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page: Int) {
        async(UI) {
            tryCatch({
                it.printStackTrace()
                // Return Throwable toString
                onHomeListListener.getHomeListFailed(it.toString())
            }) {
                homeListAsync?.cancelByActive()
                homeListAsync = RetrofitHelper.retrofitService.getHomeList(page)
                // Get async result
                val result = homeListAsync?.await()
                result ?: let {
                    onHomeListListener.getHomeListFailed(Constant.RESULT_NULL)
                    return@async
                }
                onHomeListListener.getHomeListSuccess(result)
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
            tryCatch({
                it.printStackTrace()
                onTypeTreeListListener.getTypeTreeListFailed(it.toString())
            }) {
                typeTreeListAsync?.cancelByActive()
                typeTreeListAsync = RetrofitHelper.retrofitService.getTypeTreeList()
                val result = typeTreeListAsync?.await()
                result ?: let {
                    onTypeTreeListListener.getTypeTreeListFailed(Constant.RESULT_NULL)
                    return@async
                }
                onTypeTreeListListener.getTypeTreeListSuccess(result)
            }
        }
    }

    /**
     * cancel TypeTree Request
     */
    override fun cancelTypeTreeRequest() {
        typeTreeListAsync?.cancelByActive()
    }

    /**
     * login
     * @param onLoginListener HomePresenter.OnLoginListener
     * @param username username
     * @param password password
     */
    override fun loginWanAndroid(
        onLoginListener: HomePresenter.OnLoginListener,
        username: String, password: String
    ) {
        async(UI) {
            tryCatch({
                it.printStackTrace()
                onLoginListener.loginFailed(it.toString())
            }) {
                loginAsync?.cancelByActive()
                loginAsync = RetrofitHelper.retrofitService.loginWanAndroid(username, password)
                // Get async result
                val result = loginAsync?.await()
                result ?: let {
                    onLoginListener.loginFailed(Constant.RESULT_NULL)
                    return@async
                }
                onLoginListener.loginSuccess(result)
            }
        }
    }

    /**
     * cancel login Request
     */
    override fun cancelLoginRequest() {
        loginAsync?.cancelByActive()
    }

    /**
     * register
     * @param onRegisterListener HomePresenter.OnRegisterListener
     * @param username username
     * @param password password
     * @param repassword repassword
     */
    override fun registerWanAndroid(
        onRegisterListener: HomePresenter.OnRegisterListener,
        username: String, password: String, repassword: String
    ) {
        async(UI) {
            tryCatch({
                it.printStackTrace()
                onRegisterListener.registerFailed(it.toString())
            }) {
                registerAsync?.cancelByActive()
                registerAsync = RetrofitHelper.retrofitService.registerWanAndroid(
                    username,
                    password,
                    repassword
                )
                // Get async result
                val result = registerAsync?.await()
                result ?: let {
                    onRegisterListener.registerFailed(Constant.RESULT_NULL)
                    return@async
                }
                onRegisterListener.registerSuccess(result)
            }
        }
    }

    /**
     * cancel register Request
     */
    override fun cancelRegisterRequest() {
        registerAsync?.cancelByActive()
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
                bookmarkListAsync?.cancelByActive()
                bookmarkListAsync = RetrofitHelper.retrofitService.getBookmarkList()
                val result = bookmarkListAsync?.await()
                result?.let {
                    bookmarkResult = it
                }
            }
            tryCatch({
                throwable = it
                it.printStackTrace()
            }) {
                hotListAsync?.cancelByActive()
                hotListAsync = RetrofitHelper.retrofitService.getHotKeyList()
                val result = hotListAsync?.await()
                result?.let {
                    hotResult = it
                }
            }
            tryCatch({
                throwable = it
                it.printStackTrace()
            }) {
                friendListAsync?.cancelByActive()
                friendListAsync = RetrofitHelper.retrofitService.getFriendList()
                val result = friendListAsync?.await()
                result?.let {
                    commonResult = it
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
    override fun collectArticle(
        onCollectArticleListener: HomePresenter.OnCollectArticleListener,
        id: Int,
        isAdd: Boolean
    ) {
        async(UI) {
            tryCatch({
                it.printStackTrace()
                onCollectArticleListener.collectArticleFailed(it.toString(), isAdd)
            }) {
                collectArticleAsync?.cancelByActive()
                collectArticleAsync = if (isAdd) {
                    RetrofitHelper.retrofitService.addCollectArticle(id)
                } else {
                    RetrofitHelper.retrofitService.removeCollectArticle(id)
                }
                val result = collectArticleAsync?.await()
                result ?: let {
                    onCollectArticleListener.collectArticleFailed(Constant.RESULT_NULL, isAdd)
                    return@async
                }
                onCollectArticleListener.collectArticleSuccess(result, isAdd)
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
            tryCatch({
                it.printStackTrace()
                onBannerListener.getBannerFailed(it.toString())
            }) {
                bannerAsync?.cancelByActive()
                bannerAsync = RetrofitHelper.retrofitService.getBanner()
                val result = bannerAsync?.await()
                result ?: let {
                    onBannerListener.getBannerFailed(Constant.RESULT_NULL)
                    return@async
                }
                onBannerListener.getBannerSuccess(result)
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