package top.jowanxu.wanandroidclient.model

import Constant
import asyncRequestSuspend
import cancelAndJoinByActive
import cancelCall
import getSearchListCall
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.jowanxu.wanandroidclient.bean.SearchListResponse
import top.jowanxu.wanandroidclient.presenter.SearchPresenter

class SearchModelImpl : SearchModel {

    private var searchListCall: Call<SearchListResponse>? = null
    private var searchListAsync: Deferred<Any>? = null

    override fun getSearchList(onSearchListListener: SearchPresenter.OnSearchListListener, page: Int, k: String) {
        async(UI) {
            try {
                searchListAsync?.cancelAndJoinByActive()
                searchListAsync = async(CommonPool) {
                    asyncRequestSuspend<SearchListResponse> { cont ->
                        searchListCall?.cancelCall()
                        getSearchListCall(page, k).apply {
                            searchListCall = this@apply
                        }.enqueue(object : Callback<SearchListResponse> {
                            override fun onFailure(call: Call<SearchListResponse>, t: Throwable) {
                                cont.resumeWithException(t)
                            }

                            override fun onResponse(call: Call<SearchListResponse>, response: Response<SearchListResponse>) {
                                cont.resume(response.body())
                            }
                        })
                    }
                }
                val result = searchListAsync?.await()
                when (result) {
                    is String -> onSearchListListener.getSearchListFailed(result)
                    is SearchListResponse -> onSearchListListener.getSearchListSuccess(result)
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
}