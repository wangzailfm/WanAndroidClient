package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.HomeListResponse

/**
 * 首页Presenter接口
 */
interface SearchPresenter {

    interface OnSearchListListener {

        fun getSearchList(page: Int = 0, k: String)

        /**
         * 获取搜索结果列表成功
         */
        fun getSearchListSuccess(result: HomeListResponse)

        /**
         * 获取搜索结果列表失败
         */
        fun getSearchListFailed(errorMessage: String?)
    }

    /**
     * get home list interface
     */
    interface OnLikeListListener {

        /**
         * get home list
         * @param page page
         */
        fun getLikeList(page: Int = 0)

        /**
         * get home list success
         * @param result result
         */
        fun getLikeListSuccess(result: HomeListResponse)

        /**
         * get home list failed
         * @param errorMessage error message
         */
        fun getLikeListFailed(errorMessage: String?)
    }
}