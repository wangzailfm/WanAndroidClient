package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.bean.TreeListResponse

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
}