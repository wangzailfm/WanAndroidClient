package top.jowanxu.wanandroidclient.model

import top.jowanxu.wanandroidclient.presenter.HomePresenter

/**
 * 首页Model接口
 */
interface HomeModel {
    /**
     * 获取首页列表
     * @param onHomeListListener HomePresenter.OnHomeListListener
     * @param page 页数
     */
    fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page: Int = 0)

    /**
     * 取消请求
     */
    fun cancelRequest()
}