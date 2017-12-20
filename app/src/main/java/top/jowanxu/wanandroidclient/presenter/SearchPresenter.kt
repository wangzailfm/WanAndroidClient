package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.SearchListResponse

/**
 * 首页Presenter接口
 */
interface SearchPresenter {

    fun getSearchList(page: Int = 0, k: String)

    interface OnSearchListListener {

        /**
         * 获取搜索结果列表成功
         */
        fun getSearchListSuccess(result: SearchListResponse)

        /**
         * 获取搜索结果列表失败
         */
        fun getSearchListFailed(errorMessage: String?)
    }
}