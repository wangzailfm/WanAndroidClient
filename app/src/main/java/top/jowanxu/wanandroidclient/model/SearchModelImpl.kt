package top.jowanxu.wanandroidclient.model

import Constant
import RetrofitHelper
import cancelAndJoinByActive
import cancelByActive
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.presenter.HomePresenter
import top.jowanxu.wanandroidclient.presenter.SearchPresenter

class SearchModelImpl : SearchModel, CollectArticleModel {

    private var searchListAsync: Deferred<Any>? = null
    /**
     * Home list async
     */
    private var likeListAsync: Deferred<Any>? = null
    /**
     * Collect Article async
     */
    private var collectArticleAsync: Deferred<Any>? = null

    override fun getSearchList(onSearchListListener: SearchPresenter.OnSearchListListener, page: Int, k: String) {
        async(UI) {
            try {
                searchListAsync?.cancelAndJoinByActive()
                searchListAsync = RetrofitHelper.retrofitService.getSearchList(page, k)
                val result = searchListAsync?.await()
                if (result is HomeListResponse) {
                    onSearchListListener.getSearchListSuccess(result)
                } else {
                    onSearchListListener.getSearchListFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onSearchListListener.getSearchListFailed(t.toString())
                return@async
            }
        }
    }

    override fun cancelRequest() {
        searchListAsync?.cancelByActive()
    }

    /**
     * get Home List
     * @param onLikeListListener SearchPresenter.OnLikeListListener
     * @param page page
     */
    override fun getLikeList(onLikeListListener: SearchPresenter.OnLikeListListener, page: Int) {
        async(UI) {
            try {
                likeListAsync?.cancelAndJoinByActive()
                likeListAsync = RetrofitHelper.retrofitService.getLikeList(page)
                // Get async result
                val result = likeListAsync?.await()
                if (result is HomeListResponse) {
                    onLikeListListener.getLikeListSuccess(result)
                } else {
                    onLikeListListener.getLikeListFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                // Return Throwable toString
                onLikeListListener.getLikeListFailed(t.toString())
                return@async
            }
        }
    }
    /**
     * cancel HomeList Request
     */
    override fun cancelLikeListRequest() {
        likeListAsync?.cancelByActive()
    }

    /**
     * add or remove collect article
     */
    override fun collectArticle(onCollectArticleListener: HomePresenter.OnCollectArticleListener, id: Int, isAdd: Boolean) {
        async(UI) {
            try {
                collectArticleAsync?.cancelAndJoinByActive()
                collectArticleAsync = if (isAdd) {
                    RetrofitHelper.retrofitService.addCollectArticle(id)
                } else {
                    RetrofitHelper.retrofitService.removeCollectArticle(id)
                }
                val result = collectArticleAsync?.await()
                if (result is HomeListResponse) {
                    onCollectArticleListener.collectArticleSuccess(result, isAdd)
                } else {
                    onCollectArticleListener.collectArticleFailed(Constant.RESULT_NULL, isAdd)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onCollectArticleListener.collectArticleFailed(t.toString(), isAdd)
                return@async
            }
        }
    }

    /**
     * cancel collect article Request
     */
    override fun cancelCollectRequest() {
        collectArticleAsync?.cancelByActive()
    }
}