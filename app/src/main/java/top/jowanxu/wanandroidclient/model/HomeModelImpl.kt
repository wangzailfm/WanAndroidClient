package top.jowanxu.wanandroidclient.model

import Constant
import asyncRequestSuspend
import cancelAndJoinByActive
import cancelCall
import getHomeListCall
import getTypeTreeList
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.bean.TreeListResponse
import top.jowanxu.wanandroidclient.presenter.HomePresenter

class HomeModelImpl : HomeModel {

    /**
     * Home list Call
     */
    private var homeListCall: Call<HomeListResponse>? = null
    /**
     * Home list async
     */
    private var homeListAsync: Deferred<Any>? = null
    /**
     * TypeTree list Call
     */
    private var typeTreeListCall: Call<TreeListResponse>? = null
    /**
     * TypeTree async
     */
    private var typeTreeListAsync: Deferred<Any>? = null
    /**
     * get Home List
     * @param onHomeListListener HomePresenter.OnHomeListListener
     * @param page page
     */
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
    /**
     * get TypeTree List
     * @param onTypeTreeListListener HomePresenter.OnTypeTreeListListener
     */
    override fun getTypeTreeList(onTypeTreeListListener: HomePresenter.OnTypeTreeListListener) {
        async(UI) {
            try {
                typeTreeListAsync?.cancelAndJoinByActive()
                typeTreeListAsync = async(CommonPool) {
                    asyncRequestSuspend<TreeListResponse> { cont ->
                        typeTreeListCall?.cancelCall()
                        getTypeTreeList().apply {
                            typeTreeListCall = this@apply
                        }.enqueue(object : Callback<TreeListResponse> {
                            override fun onResponse(call: Call<TreeListResponse>?, response: Response<TreeListResponse>) {
                                cont.resume(response.body())
                            }
                            override fun onFailure(call: Call<TreeListResponse>?, t: Throwable) {
                                cont.resumeWithException(t)
                            }
                        })
                    }
                }
                val result = typeTreeListAsync?.await()
                when (result) {
                    is String -> onTypeTreeListListener.getTypeTreeListFailed(result)
                    is TreeListResponse -> onTypeTreeListListener.getTypeTreeListSuccess(result)
                    else -> onTypeTreeListListener.getTypeTreeListFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onTypeTreeListListener.getTypeTreeListFailed(t.toString())
                return@async
            }
        }
    }
    /**
     * cancel HomeList Request
     */
    override fun cancelHomeListRequest() {
        homeListCall?.cancel()
        homeListAsync?.cancel()
    }
    /**
     * cancel TypeTree Request
     */
    override fun cancelTypeTreeRequest() {
        typeTreeListCall?.cancel()
        typeTreeListAsync?.cancel()
    }
}