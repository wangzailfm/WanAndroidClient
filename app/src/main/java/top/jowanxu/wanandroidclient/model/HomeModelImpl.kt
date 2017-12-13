package top.jowanxu.wanandroidclient.model

import Constant
import asyncRequestSuspend
import getHomeListCall
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelAndJoin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.jowanxu.wanandroidclient.bean.HomeListModel
import top.jowanxu.wanandroidclient.presenter.HomePresenter

class HomeModelImpl : HomeModel {
    private var onHomeListListener: HomePresenter.OnHomeListListener? = null

    /**
     * Home Call
     */
    private var homeListCall: Call<HomeListModel>? = null

    /**
     * async return coroutine result
     */
    private var homeListAsync: Deferred<Any>? = null

    override fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page: Int) {
        this.onHomeListListener ?: let {
            this.onHomeListListener = onHomeListListener
        }
        async(UI) {
            homeListAsync?.apply {
                if (isActive) {
                    cancelAndJoin()
                }
            }
            homeListAsync = async(CommonPool) {
                try {
                    // Async Request, wait resume
                    asyncRequestSuspend<HomeListModel> { cont ->
                        homeListCall?.run {
                            // If Call not empty
                            if (!isCanceled) {
                                // cancel request
                                cancel()
                            }
                            // Assignment
                            getHomeListCall()
                        } ?: run {
                            // If Call empty
                            getHomeListCall()
                        }.enqueue(object : Callback<HomeListModel> {
                            override fun onResponse(call: Call<HomeListModel>,
                                                    response: Response<HomeListModel>) {
                                // resume
                                cont.resume(response.body())
                            }

                            override fun onFailure(call: Call<HomeListModel>, t: Throwable) {
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
                is HomeListModel -> onHomeListListener.getHomeListSuccess(result)
                else -> onHomeListListener.getHomeListFailed(Constant.RESULT_NULL)
            }
        }

    }
}