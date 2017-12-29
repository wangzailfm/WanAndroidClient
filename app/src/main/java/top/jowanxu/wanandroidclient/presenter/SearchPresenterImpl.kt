package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.model.CollectArticleModel
import top.jowanxu.wanandroidclient.model.SearchModel
import top.jowanxu.wanandroidclient.model.SearchModelImpl
import top.jowanxu.wanandroidclient.view.CollectArticleView
import top.jowanxu.wanandroidclient.view.SearchListView

class SearchPresenterImpl(private val searchView: SearchListView, private val collectArticleView: CollectArticleView)
    : SearchPresenter.OnSearchListListener, SearchPresenter.OnLikeListListener, HomePresenter.OnCollectArticleListener {

    private val searchModel: SearchModel = SearchModelImpl()
    private val collectArticleModel: CollectArticleModel = SearchModelImpl()

    override fun getSearchList(page: Int, k: String) {
        searchModel.getSearchList(this, page, k)
    }

    override fun getSearchListSuccess(result: HomeListResponse) {
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
        searchView.getLikeListFailed(errorMessage)
    }

    /**
     *  add or remove collect article
     *  @param id article id
     *  @param isAdd true add, false remove
     */
    override fun collectArticle(id: Int, isAdd: Boolean) {
        collectArticleModel.collectArticle(this, id, isAdd)
    }

    /**
     * add collect article success
     * @param result HomeListResponse
     * @param isAdd true add, false remove
     */
    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        if (result.errorCode != 0) {
            collectArticleView.collectArticleFailed(result.errorMsg, isAdd)
        } else {
            collectArticleView.collectArticleSuccess(result, isAdd)
        }
    }

    /**
     * add collect article failed
     * @param errorMessage error message
     * @param isAdd true add, false remove
     */
    override fun collectArticleFailed(errorMessage: String?, isAdd: Boolean) {
        collectArticleView.collectArticleFailed(errorMessage, isAdd)
    }

    fun cancelRequest() {
        searchModel.cancelRequest()
        collectArticleModel.cancelCollectRequest()
    }
}
