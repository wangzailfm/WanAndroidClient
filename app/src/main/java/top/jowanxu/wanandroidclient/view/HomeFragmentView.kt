package top.jowanxu.wanandroidclient.view

import top.jowanxu.wanandroidclient.bean.HomeListResponse

/**
 * Home Fragment View interface
 */
interface HomeFragmentView {
    /**
     * get Home list Success
     */
    fun getHomeListSuccess(result: HomeListResponse)
    /**
     * get Home list Failed
     */
    fun getHomeListFailed(errorMessage: String?)
    /**
     * get Home list data size equal zero
     */
    fun getHomeListZero()
    /**
     * get Home list data less than 20
     */
    fun getHomeListSmall(result: HomeListResponse)
}