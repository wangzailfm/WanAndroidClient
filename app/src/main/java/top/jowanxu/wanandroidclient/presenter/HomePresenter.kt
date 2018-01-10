package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.*

/**
 * 首页Presenter接口
 */
interface HomePresenter {

    /**
     * get home list interface
     */
    interface OnHomeListListener {

        /**
         * get home list
         * @param page page
         */
        fun getHomeList(page: Int = 0)

        /**
         * get home list success
         * @param result result
         */
        fun getHomeListSuccess(result: HomeListResponse)

        /**
         * get home list failed
         * @param errorMessage error message
         */
        fun getHomeListFailed(errorMessage: String?)
    }

    /**
     * get type tree list interface
     */
    interface OnTypeTreeListListener {

        /**
         * get type tree list
         */
        fun getTypeTreeList()

        /**
         * get type tree list success
         * @param result result
         */
        fun getTypeTreeListSuccess(result: TreeListResponse)

        /**
         * get type tree list failed
         * @param errorMessage error message
         */
        fun getTypeTreeListFailed(errorMessage: String?)
    }

    /**
     * login Listener
     */
    interface OnLoginListener {
        /**
         * login
         * @param username username
         * @param password password
         */
        fun loginWanAndroid(username: String, password: String)

        /**
         * login success
         * @param result LoginResponse
         */
        fun loginSuccess(result: LoginResponse)

        /**
         * login failed
         * @param errorMessage error message
         */
        fun loginFailed(errorMessage: String?)
    }

    /**
     * register Listener
     */
    interface OnRegisterListener {
        /**
         * register
         * @param username username
         * @param password password
         * @param repassword repassword
         */
        fun registerWanAndroid(username: String, password: String, repassword: String)

        /**
         * register success
         * @param result LoginResponse
         */
        fun registerSuccess(result: LoginResponse)

        /**
         * register failed
         * @param errorMessage error message
         */
        fun registerFailed(errorMessage: String?)
    }

    /**
     * get friend list interface
     */
    interface OnFriendListListener {

        /**
         * get friend tree list
         */
        fun getFriendList()

        /**
         * get friend list success
         */
        fun getFriendListSuccess(bookmarkResult: FriendListResponse?, commonResult: FriendListResponse, hotResult: HotKeyResponse)

        /**
         * get friend list failed
         * @param errorMessage error message
         */
        fun getFriendListFailed(errorMessage: String?)
    }

    /**
     * collect article listener
     */
    interface OnCollectArticleListener {
        /**
         *  add or remove collect article
         *  @param id article id
         *  @param isAdd true add, false remove
         */
        fun collectArticle(id: Int, isAdd: Boolean)


        /**
         * add collect article success
         * @param result HomeListResponse
         * @param isAdd true add, false remove
         */
        fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean)

        /**
         * add collect article failed
         * @param errorMessage error message
         * @param isAdd true add, false remove
         */
        fun collectArticleFailed(errorMessage: String?, isAdd: Boolean)
    }

    /**
     * collect outside article listener
     */
    interface OnCollectOutsideArticleListener {
        /**
         *  add or remove outside collect article
         *  @param title article title
         *  @param author article author
         *  @param link article link
         *  @param isAdd true add, false remove
         */
        fun collectOutSideArticle(title: String, author: String, link: String, isAdd: Boolean)

        /**
         * add collect outside article success
         * @param result HomeListResponse
         * @param isAdd true add, false remove
         */
        fun collectOutsideArticleSuccess(result: HomeListResponse, isAdd: Boolean)

        /**
         * add collect outside article failed
         * @param errorMessage error message
         * @param isAdd true add, false remove
         */
        fun collectOutsideArticleFailed(errorMessage: String?, isAdd: Boolean)
    }

    /**
     * get banner listener
     */
    interface OnBannerListener {
        /**
         * get banner
         */
        fun getBanner()

        /**
         * get banner success
         * @param result BannerResponse
         */
        fun getBannerSuccess(result: BannerResponse)

        /**
         * get banner failed
         * @param errorMessage error message
         */
        fun getBannerFailed(errorMessage: String?)
    }

    /**
     * get friend list interface
     */
    interface OnBookmarkListListener {

        /**
         * get friend tree list
         */
        fun getFriendList()

        /**
         * get friend list success
         * @param result result
         */
        fun getFriendListSuccess(result: FriendListResponse)

        /**
         * get friend list failed
         * @param errorMessage error message
         */
        fun getFriendListFailed(errorMessage: String?)
    }
}