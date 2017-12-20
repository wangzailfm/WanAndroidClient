package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.SearchListResponse
import top.jowanxu.wanandroidclient.model.SearchModel
import top.jowanxu.wanandroidclient.model.SearchModelImpl
import top.jowanxu.wanandroidclient.view.SearchListView

class SearchPresenterImpl(private val searchView: SearchListView) : SearchPresenter, SearchPresenter.OnSearchListListener {

    private val searchModel: SearchModel = SearchModelImpl()

    override fun getSearchList(page: Int, k: String) {
        searchModel.getSearchList(this, page, k)
    }

    override fun getSearchListSuccess(result: SearchListResponse) {
        searchView.getSearchListSuccess(result)
    }

    override fun getSearchListFailed(errorMessage: String?) {
        searchView.getSearchListFailed(errorMessage)
    }

    fun cancelRequest() {
        searchModel.cancelRequest()
    }
}
