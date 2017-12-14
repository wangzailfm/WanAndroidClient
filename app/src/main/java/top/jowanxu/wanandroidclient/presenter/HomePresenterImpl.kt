package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.model.HomeModel
import top.jowanxu.wanandroidclient.model.HomeModelImpl
import top.jowanxu.wanandroidclient.view.HomeView

class HomePresenterImpl(val homeView: HomeView) : HomePresenter, HomePresenter.OnHomeListListener {

    private val homeModel: HomeModel = HomeModelImpl()

    override fun getHomeList(page: Int) {
        homeModel.getHomeList(this, page)
    }

    override fun getHomeListSuccess(result: HomeListResponse) {
        homeView.getHomeListSuccess(result)
    }

    override fun getHomeListFailed(errorMessage: String?) {
        homeView.getHomeListFailed(errorMessage)
    }

    fun cancelRequest() {
        homeModel.cancelRequest()
    }
}
