package top.jowanxu.wanandroidclient.view

import top.jowanxu.wanandroidclient.bean.ArticleListResponse

interface TypeArticleFragmentView {
    /**
     * get Type Article list Success
     * @param result ArticleListResponse
     */
    fun getTypeArticleListSuccess(result: ArticleListResponse)
    /**
     * get Type Article list Failed
     * @param errorMessage error message
     */
    fun getTypeArticleListFailed(errorMessage: String?)
    /**
     * get Type Article list data size equal zero
     */
    fun getTypeArticleListZero()
    /**
     * get Type Article list data less than 20
     * @param result ArticleListResponse
     */
    fun getTypeArticleListSmall(result: ArticleListResponse)
}