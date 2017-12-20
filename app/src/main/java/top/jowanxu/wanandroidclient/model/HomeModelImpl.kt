package top.jowanxu.wanandroidclient.model

import Constant
import asyncRequestSuspend
import cancelAndJoinByActive
import cancelCall
import getHomeListCall
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.presenter.HomePresenter

class HomeModelImpl : HomeModel {

    /**
     * Home Call
     */
    private var homeListCall: Call<HomeListResponse>? = null

    /**
     * async return coroutine result
     */
    private var homeListAsync: Deferred<Any>? = null

    override fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page: Int) {
        async(UI) {
            try {
                homeListAsync?.cancelAndJoinByActive()
                homeListAsync = async(CommonPool) {
                    // Async Request, wait resume
                    asyncRequestSuspend<HomeListResponse> { cont ->
                        homeListCall?.cancelCall()
                        getHomeListCall(page).apply {
                            homeListCall = this@apply
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
                val result = homeListAsync?.await()
                when (result) {
                    is String -> onHomeListListener.getHomeListFailed(result)
                    is HomeListResponse -> onHomeListListener.getHomeListSuccess(result)
                    else -> onHomeListListener.getHomeListFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                // Return Throwable toString
                onHomeListListener.getHomeListFailed(t.toString())
                return@async
            }
        }

    }

    override fun cancelRequest() {
        homeListCall?.cancel()
        homeListAsync?.cancel()
    }
}