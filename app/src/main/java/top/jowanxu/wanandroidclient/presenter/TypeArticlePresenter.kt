package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.ArticleListResponse

interface TypeArticlePresenter {
    /**
     * get Type Article list
     * @param page page
     * @param cid cid
     */
    fun getTypeArticleList(page: Int = 0, cid: Int)

    /**
     * get Type Article list interface
     */
    interface OnTypeArticleListListener {
        /**
         * get Type Article list success
         * @param result ArticleListResponse
         */
        fun getTypeArticleListSuccess(result: ArticleListResponse)

        /**
         * get Type Article list failed
         * @param errorMessage error message
         */
        fun getTypeArticleListFailed(errorMessage: String?)
    }
}