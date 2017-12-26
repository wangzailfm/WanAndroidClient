package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.model.SearchModel
import top.jowanxu.wanandroidclient.model.SearchModelImpl
import top.jowanxu.wanandroidclient.view.SearchListView

class SearchPresenterImpl(private val searchView: SearchListView) : SearchPresenter.OnSearchListListener, SearchPresenter.OnLikeListListener {

    private val searchModel: SearchModel = SearchModelImpl()

    override fun getSearchList(page: Int, k: String) {
        searchModel.getSearchList(this, page, k)
    }

    override fun getSearchListSuccess(result: HomeListResponse) {
        searchView.getSearchListAfter()
        if (result.errorCode != 0) {
            searchView.getSearchListFailed(result.errorMsg)
            return
        }
        // 列表总数
        val total = result.data.total
        if (total == 0) {
            searchView.getSearchListZero()
            return
        }
        // 当第一页小于一页总数时
        if (total < result.data.size) {
            searchView.getSearchListSmall(result)
            return
        }
        searchView.getSearchListSuccess(result)
    }

    override fun getSearchListFailed(errorMessage: String?) {
        searchView.getSearchListFailed(errorMessage)
    }

    /**
     * get home list
     * @param page page
     */
    override fun getLikeList(page: Int) {
        searchModel.getLikeList(this, page)
    }

    /**
     * get home list success
     * @param result result
     */
    override fun getLikeListSuccess(result: HomeListResponse) {
        searchView.getLikeListAfter()
        if (result.errorCode != 0) {
            searchView.getLikeListFailed(result.errorMsg)
            return
        }
        // 列表总数
        val total = result.data.total
        if (total == 0) {
            searchView.getLikeListZero()
            return
        }
        // 当第一页小于一页总数时
        if (total < result.data.size) {
            searchView.getLikeListSmall(result)
            return
        }
        searchView.getLikeListSuccess(result)
    }

    /**
     * get home list failed
     * @param errorMessage error message
     */
    override fun getLikeListFailed(errorMessage: String?) {
        searchView.getLikeListAfter()
        searchView.getLikeListFailed(errorMessage)
    }

    fun cancelRequest() {
        searchModel.cancelRequest()
    }
}
