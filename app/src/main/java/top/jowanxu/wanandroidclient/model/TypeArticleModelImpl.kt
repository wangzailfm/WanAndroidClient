package top.jowanxu.wanandroidclient.model

import Constant
import RetrofitHelper
import cancelAndJoinByActive
import cancelByActive
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import top.jowanxu.wanandroidclient.bean.ArticleListResponse
import top.jowanxu.wanandroidclient.presenter.TypeArticlePresenter

class TypeArticleModelImpl : TypeArticleModel {
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
                typeArticleListAsync = RetrofitHelper.retrofitService.getArticleList(page, cid)
                val result = typeArticleListAsync?.await()
                if (result is ArticleListResponse) {
                    onTypeArticleListListener.getTypeArticleListSuccess(result)
                } else {
                    onTypeArticleListListener.getTypeArticleListFailed(Constant.RESULT_NULL)
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
        typeArticleListAsync?.cancelByActive()
    }
}