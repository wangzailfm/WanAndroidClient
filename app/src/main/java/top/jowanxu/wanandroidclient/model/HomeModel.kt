package top.jowanxu.wanandroidclient.model

import top.jowanxu.wanandroidclient.presenter.HomePresenter

/**
 * 首页Model接口
 */
interface HomeModel {
    /**
     * get Home List
     * @param onHomeListListener HomePresenter.OnHomeListListener
     * @param page page
     */
    fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page: Int = 0)

    /**
     * get TypeTree List
     * @param onTypeTreeListListener HomePresenter.OnTypeTreeListListener
     */
    fun getTypeTreeList(onTypeTreeListListener: HomePresenter.OnTypeTreeListListener)

    /**
     * cancel HomeList Request
     */
    fun cancelHomeListRequest()
    /**
     * cancel TypeTree Request
     */
    fun cancelTypeTreeRequest()
}