package top.jowanxu.wanandroidclient.model

import top.jowanxu.wanandroidclient.presenter.TypeArticlePresenter

interface TypeArticleModel {
    /**
     * get Type Article list
     * @param onTypeArticleListListener TypeArticlePresenter.OnTypeArticleListListener
     * @param page page
     * @param cid cid
     */
    fun getTypeArticleList(onTypeArticleListListener: TypeArticlePresenter.OnTypeArticleListListener, page: Int = 0, cid: Int)

    /**
     * cancel request
     */
    fun cancelRequest()
}