package top.jowanxu.wanandroidclient.model

import top.jowanxu.wanandroidclient.presenter.SearchPresenter

/**
 * 首页Model接口
 */
interface SearchModel {

    /**
     * 获取搜索结果列表
     */
    fun getSearchList(onSearchListListener: SearchPresenter.OnSearchListListener, page: Int = 0, k: String)

    /**
     * 取消请求
     */
    fun cancelRequest()
}