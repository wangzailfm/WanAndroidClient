package top.jowanxu.wanandroidclient.model

import Constant
import asyncRequestSuspend
import cancelAndJoinByActive
import cancelCall
import collectArticleCall
import getLikeListCall
import getSearchListCall
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.presenter.HomePresenter
import top.jowanxu.wanandroidclient.presenter.SearchPresenter

class SearchModelImpl : SearchModel, CollectArticleModel {

    private var searchListCall: Call<HomeListResponse>? = null
    private var searchListAsync: Deferred<Any>? = null

    /**
     * Home list Call
     */
    private var likeListCall: Call<HomeListResponse>? = null
    /**
     * Home list async
     */
    private var likeListAsync: Deferred<Any>? = null
    /**
     * Collect Article Call
     */
    private var collectArticleCall: Call<HomeListResponse>? = null
    /**
     * Collect Article async
     */
    private var collectArticleAsync: Deferred<Any>? = null

    override fun getSearchList(onSearchListListener: SearchPresenter.OnSearchListListener, page: Int, k: String) {
        async(UI) {
            try {
                searchListAsync?.cancelAndJoinByActive()
                searchListAsync = async(CommonPool) {
                    asyncRequestSuspend<HomeListResponse> { cont ->
                        searchListCall?.cancelCall()
                        getSearchListCall(page, k).apply {
                            searchListCall = this@apply
                        }.enqueue(object : Callback<HomeListResponse> {
                            override fun onFailure(call: Call<HomeListResponse>, t: Throwable) {
                                cont.resumeWithException(t)
                            }

                            override fun onResponse(call: Call<HomeListResponse>, response: Response<HomeListResponse>) {
                                cont.resume(response.body())
                            }
                        })
                    }
                }
                val result = searchListAsync?.await()
                when (result) {
                    is String -> onSearchListListener.getSearchListFailed(result)
                    is HomeListResponse -> onSearchListListener.getSearchListSuccess(result)
                    else -> onSearchListListener.getSearchListFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onSearchListListener.getSearchListFailed(t.toString())
                return@async
            }
        }
    }

    override fun cancelRequest() {
        searchListCall?.cancel()
        searchListAsync?.cancel()
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
                likeListAsync = async(CommonPool) {
                    // Async Request, wait resume
                    asyncRequestSuspend<HomeListResponse> { cont ->
                        likeListCall?.cancelCall()
                        getLikeListCall(page).apply {
                            likeListCall = this@apply
                        }.enqueue(object : Callback<HomeListResponse> {
                            override fun onResponse(call: Call<HomeListResponse>,
                                                    response: Response<HomeListResponse>) {
                                // resume
                                cont.resume(response.body())
                            }

                            override fun onFailure(call: Call<HomeListResponse>, t: Throwable) {
                                cont.resumeWithException(t)
                            }
                        })
                    }
                }
                // Get async result
                val result = likeListAsync?.await()
                when (result) {
                    is String -> onLikeListListener.getLikeListFailed(result)
                    is HomeListResponse -> onLikeListListener.getLikeListSuccess(result)
                    else -> onLikeListListener.getLikeListFailed(Constant.RESULT_NULL)
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
        likeListCall?.cancel()
        likeListAsync?.cancel()
    }

    /**
     * add or remove collect article
     */
    override fun collectArticle(onCollectArticleListener: HomePresenter.OnCollectArticleListener, id: Int, isAdd: Boolean) {
        async(UI) {
            try {
                collectArticleAsync?.cancelAndJoinByActive()
                collectArticleAsync = async(CommonPool) {
                    asyncRequestSuspend<HomeListResponse> { cont ->
                        collectArticleCall?.cancelCall()
                        collectArticleCall(id, isAdd).apply {
                            collectArticleCall = this@apply
                        }.enqueue(object : Callback<HomeListResponse> {
                            override fun onResponse(call: Call<HomeListResponse>,
                                                    response: Response<HomeListResponse>) {
                                cont.resume(response.body())
                            }

                            override fun onFailure(call: Call<HomeListResponse>, t: Throwable) {
                                cont.resumeWithException(t)
                            }
                        })
                    }
                }
                val result = collectArticleAsync?.await()
                when (result) {
                    is String -> onCollectArticleListener.collectArticleFailed(result, isAdd)
                    is HomeListResponse -> onCollectArticleListener.collectArticleSuccess(result, isAdd)
                    else -> onCollectArticleListener.collectArticleFailed(Constant.RESULT_NULL, isAdd)
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
        collectArticleCall.cancelCall()
        collectArticleAsync?.cancel()
    }
}