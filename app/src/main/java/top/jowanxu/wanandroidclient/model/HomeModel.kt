package top.jowanxu.wanandroidclient.model

import top.jowanxu.wanandroidclient.presenter.HomePresenter

/**
 * 首页Model接口
 */
interface HomeModel {
    /**
     * 获取首页列表
     */
    fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page: Int = 0)
}