package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.BannerResponse
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.model.CollectArticleModel
import top.jowanxu.wanandroidclient.model.HomeModel
import top.jowanxu.wanandroidclient.model.HomeModelImpl
import top.jowanxu.wanandroidclient.view.CollectArticleView
import top.jowanxu.wanandroidclient.view.HomeFragmentView

class HomeFragmentPresenterImpl(private val homeFragmentView: HomeFragmentView,
                                private val collectArticleView: CollectArticleView) : HomePresenter.OnHomeListListener, HomePresenter.OnCollectArticleListener, HomePresenter.OnBannerListener {

    private val homeModel: HomeModel = HomeModelImpl()
    private val collectArticleModel: CollectArticleModel = HomeModelImpl()
    /**
     * get home list
     * @param page page
     */
    override fun getHomeList(page: Int) {
        homeModel.getHomeList(this, page)
    }

    /**
     * get home list success
     * @param result result
     */
    override fun getHomeListSuccess(result: HomeListResponse) {
        if (result.errorCode != 0) {
            homeFragmentView.getHomeListFailed(result.errorMsg)
            return
        }
        // 列表总数
        val total = result.data.total
        if (total == 0) {
            homeFragmentView.getHomeListZero()
            return
        }
        // 当第一页小于一页总数时
        if (total < result.data.size) {
            homeFragmentView.getHomeListSmall(result)
            return
        }
        homeFragmentView.getHomeListSuccess(result)
    }

    /**
     * get home list failed
     * @param errorMessage error message
     */
    override fun getHomeListFailed(errorMessage: String?) {
        homeFragmentView.getHomeListFailed(errorMessage)
    }

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
     * get banner
     */
    override fun getBanner() {
        homeModel.getBanner(this)
    }

    /**
     * get banner success
     * @param result BannerResponse
     */
    override fun getBannerSuccess(result: BannerResponse) {
        if (result.errorCode != 0) {
            homeFragmentView.getBannerFailed(result.errorMsg)
            return
        }
        result.data ?: let {
            homeFragmentView.getBannerZero()
            return
        }
        homeFragmentView.getBannerSuccess(result)
    }

    /**
     * get banner failed
     * @param errorMessage error message
     */
    override fun getBannerFailed(errorMessage: String?) {
        homeFragmentView.getBannerFailed(errorMessage)
    }

    /**
     * cancel request
     */
    fun cancelRequest() {
        homeModel.cancelHomeListRequest()
        collectArticleModel.cancelCollectRequest()
    }
}
