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
         * @param result result
         */
        fun getFriendListSuccess(result: FriendListResponse, hotResult: HotKeyResponse)

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
}