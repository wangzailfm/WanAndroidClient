package top.jowanxu.wanandroidclient.view

import top.jowanxu.wanandroidclient.bean.HomeListModel

/**
 * 首页View接口
 */
interface HomeView {

    /**
     * 获取首页列表成功
     */
    fun getHomeListSuccess(result: HomeListModel)

    /**
     * 获取首页列表失败
     */
    fun getHomeListFailed(errorMessage: String?)
}