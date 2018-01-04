package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.model.CollectArticleModel
import top.jowanxu.wanandroidclient.model.CollectOutsideArticleModel
import top.jowanxu.wanandroidclient.model.CollectOutsideArticleModelImpl
import top.jowanxu.wanandroidclient.model.SearchModelImpl
import top.jowanxu.wanandroidclient.view.CollectArticleView

class ContentPresenterImpl(private val collectArticleView: CollectArticleView)
    : HomePresenter.OnCollectArticleListener, HomePresenter.OnCollectOutsideArticleListener {

    private val collectArticleModel: CollectArticleModel = SearchModelImpl()
    private val collectOutsideArticleModel: CollectOutsideArticleModel = CollectOutsideArticleModelImpl()

    /**
     *  add or remove collect article
     *  @param id article id
     *  @param isAdd true add, false remove
     */
    override fun collectArticle(id: Int, isAdd: Boolean) {
        collectArticleModel.collectArticle(this, id, isAdd)
    }

    /**
     * add collect article success
     * @param result HomeListResponse
     * @param isAdd true add, false remove
     */
    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        if (result.errorCode != 0) {
            collectArticleView.collectArticleFailed(result.errorMsg, isAdd)
        } else {
            collectArticleView.collectArticleSuccess(result, isAdd)
        }
    }

    /**
     * add collect article failed
     * @param errorMessage error message
     * @param isAdd true add, false remove
     */
    override fun collectArticleFailed(errorMessage: String?, isAdd: Boolean) {
        collectArticleView.collectArticleFailed(errorMessage, isAdd)
    }

    /**
     *  add or remove outside collect article
     *  @param title article title
     *  @param author article author
     *  @param link article link
     *  @param isAdd true add, false remove
     */
    override fun collectOutSideArticle(title: String, author: String, link: String, isAdd: Boolean) {
        collectOutsideArticleModel.collectOutsideArticle(this, title, author, link, isAdd)
    }

    /**
     * add collect outside article success
     * @param result HomeListResponse
     * @param isAdd true add, false remove
     */
    override fun collectOutsideArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        if (result.errorCode != 0) {
            collectArticleView.collectArticleFailed(result.errorMsg, isAdd)
        } else {
            collectArticleView.collectArticleSuccess(result, isAdd)
        }
    }

    /**
     * add collect outside article failed
     * @param errorMessage error message
     * @param isAdd true add, false remove
     */
    override fun collectOutsideArticleFailed(errorMessage: String?, isAdd: Boolean) {
        collectArticleView.collectArticleFailed(errorMessage, isAdd)
    }

    fun cancelRequest() {
        collectArticleModel.cancelCollectRequest()
        collectOutsideArticleModel.cancelCollectOutsideRequest()
    }
}