package top.jowanxu.wanandroidclient.model

import Constant
import asyncRequestSuspend
import cancelAndJoinByActive
import cancelCall
import getArticleListCall
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.jowanxu.wanandroidclient.bean.ArticleListResponse
import top.jowanxu.wanandroidclient.presenter.TypeArticlePresenter

class TypeArticleModelImpl : TypeArticleModel {

    /**
     * Type Article list Call
     */
    private var typeArticleCall: Call<ArticleListResponse>? = null
    /**
     * Type Article list list async
     */
    private var typeArticleListAsync: Deferred<Any>? = null
    /**
     * get Type Article list
     * @param onTypeArticleListListener TypeArticlePresenter.OnTypeArticleListListener
     * @param page page
     * @param cid cid
     */
    override fun getTypeArticleList(onTypeArticleListListener: TypeArticlePresenter.OnTypeArticleListListener, page: Int, cid: Int) {
        async(UI) {
            try {
                typeArticleListAsync?.cancelAndJoinByActive()
                typeArticleListAsync = async(CommonPool) {
                    asyncRequestSuspend<ArticleListResponse> { cont ->
                        typeArticleCall?.cancelCall()
                        getArticleListCall(page, cid).apply {
                            typeArticleCall = this@apply
                        }.enqueue(object : Callback<ArticleListResponse> {
                            override fun onResponse(call: Call<ArticleListResponse>, response: Response<ArticleListResponse>) {
                                cont.resume(response.body())
                            }

                            override fun onFailure(call: Call<ArticleListResponse>, t: Throwable) {
                                cont.resumeWithException(t)
                            }
                        })
                    }
                }
                val result = typeArticleListAsync?.await()
                when (result) {
                    is String -> onTypeArticleListListener.getTypeArticleListFailed(result)
                    is ArticleListResponse -> onTypeArticleListListener.getTypeArticleListSuccess(result)
                    else -> onTypeArticleListListener.getTypeArticleListFailed(Constant.RESULT_NULL)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onTypeArticleListListener.getTypeArticleListFailed(t.toString())
                return@async
            }
        }
    }

    /**
     * cancel request
     */
    override fun cancelRequest() {
        typeArticleCall?.cancel()
        typeArticleListAsync?.cancel()
    }
}