package top.jowanxu.wanandroidclient.model

import Constant
import RetrofitHelper
import cancelAndJoinByActive
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.presenter.HomePresenter

class CollectOutsideArticleModelImpl : CollectOutsideArticleModel {
    /**
     * Collect Article async
     */
    private var collectArticleAsync: Deferred<Any>? = null

    /**
     * add or remove collect outside article
     * @param onCollectOutsideArticleListener HomePresenter.OnCollectOutsideArticleListener
     * @param title article title
     * @param author article author
     * @param link article link
     * @param isAdd true add, false remove
     */
    override fun collectOutsideArticle(onCollectOutsideArticleListener: HomePresenter.OnCollectOutsideArticleListener,
                                       title: String, author: String, link: String, isAdd: Boolean) {
        async(UI) {
            try {
                collectArticleAsync?.cancelAndJoinByActive()
                // add article
                collectArticleAsync = RetrofitHelper.retrofitService.addCollectOutsideArticle(title, author, link)
                // TODO if isAdd false, remove article
                // collectArticleAsync = RetrofitHelper.retrofitService.removeCollectArticle(id)
                val result = collectArticleAsync?.await()
                if (result is HomeListResponse) {
                    onCollectOutsideArticleListener.collectOutsideArticleSuccess(result, isAdd)
                } else {
                    onCollectOutsideArticleListener.collectOutsideArticleFailed(Constant.RESULT_NULL, isAdd)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                onCollectOutsideArticleListener.collectOutsideArticleFailed(t.toString(), isAdd)
                return@async
            }
        }
    }

    /**
     * cancel collect article Request
     */
    override fun cancelCollectOutsideRequest() {
    }
}