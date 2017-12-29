package top.jowanxu.wanandroidclient.view

import top.jowanxu.wanandroidclient.bean.HomeListResponse

/**
 * 搜索View接口
 */
interface SearchListView {
    /**
     * get search result list success
     */
    fun getSearchListSuccess(result: HomeListResponse)
    /**
     * 获取搜索结果列表失败
     */
    fun getSearchListFailed(errorMessage: String?)
    /**
     * get search result list data size equal zero
     */
    fun getSearchListZero()
    /**
     * get search result list data less than 20
     */
    fun getSearchListSmall(result: HomeListResponse)
    /**
     * get Home list Success
     */
    fun getLikeListSuccess(result: HomeListResponse)
    /**
     * get Home list Failed
     */
    fun getLikeListFailed(errorMessage: String?)
    /**
     * get Home list data size equal zero
     */
    fun getLikeListZero()
    /**
     * get Home list data less than 20
     */
    fun getLikeListSmall(result: HomeListResponse)

}