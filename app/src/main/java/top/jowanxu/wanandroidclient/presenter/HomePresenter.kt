package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.HomeListModel

/**
 * 首页Presenter接口
 */
interface HomePresenter {

    /**
     * 获取首页列表
     */
    fun getHomeList(page: Int = 0)

    /**
     * 首页列表接口
     */
    interface OnHomeListListener {

        /**
         * 获取首页列表成功
         */
        fun getHomeListSuccess(result: HomeListModel)

        /**
         * 获取首页列表失败
         */
        fun getHomeListFailed(errorMessage: String?)
    }
}