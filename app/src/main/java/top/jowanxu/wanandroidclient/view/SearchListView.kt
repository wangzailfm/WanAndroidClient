package top.jowanxu.wanandroidclient.view

import top.jowanxu.wanandroidclient.bean.SearchListResponse

/**
 * 搜索View接口
 */
interface SearchListView {

    /**
     * 获取搜索结果列表成功
     */
    fun getSearchListSuccess(result: SearchListResponse)

    /**
     * 获取搜索结果列表失败
     */
    fun getSearchListFailed(errorMessage: String?)

}