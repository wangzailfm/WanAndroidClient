package top.jowanxu.wanandroidclient.model

import Constant
import asyncRequestSuspend
import getHomeListCall
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.presenter.HomePresenter

class HomeModelImpl : HomeModel {
    private var onHomeListListener: HomePresenter.OnHomeListListener? = null

    /**
     * Home Call
     */
    private var homeListCall: Call<HomeListResponse>? = null

    /**
     * async return coroutine result
     */
    private var homeListAsync: Deferred<Any>? = null

    override fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page: Int) {
        this.onHomeListListener ?: let {
            this.onHomeListListener = onHomeListListener
        }
        async(UI) {
            delay(2000)
            homeListAsync?.apply {
                if (isActive) {
                    cancelAndJoin()
                }
            }
            homeListAsync = async(CommonPool) {
                try {
                    // Async Request, wait resume
                    asyncRequestSuspend<HomeListResponse> { cont ->
                        homeListCall?.run {
                            // If Call not empty
                            if (!isCanceled) {
                                // cancel request
                                cancel()
                            }
                            // Assignment
                            getHomeListCall(page)
                        } ?: run {
                            // If Call empty
                            getHomeListCall(page)
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
                } catch (e: Throwable) {
                    // Return Throwable toString
                    e.toString()
                }
            }
            // Get async result
            val result = homeListAsync?.await()
            when (result) {
                is String -> onHomeListListener.getHomeListFailed(result)
                is HomeListResponse -> onHomeListListener.getHomeListSuccess(result)
                else -> onHomeListListener.getHomeListFailed(Constant.RESULT_NULL)
            }
        }

    }

    override fun cancelRequest() {
        homeListCall?.cancel()
        homeListAsync?.cancel()
    }
}