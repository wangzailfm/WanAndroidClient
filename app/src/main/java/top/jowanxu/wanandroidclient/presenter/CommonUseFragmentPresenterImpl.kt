package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.FriendListResponse
import top.jowanxu.wanandroidclient.model.HomeModel
import top.jowanxu.wanandroidclient.model.HomeModelImpl
import top.jowanxu.wanandroidclient.view.CommonUseFragmentView

class CommonUseFragmentPresenterImpl(private val commonUseFragmentView: CommonUseFragmentView) : HomePresenter.OnFriendListListener {

    private val homeModel: HomeModel = HomeModelImpl()
    /**
     * get friend tree list
     */
    override fun getFriendList() {
        homeModel.getFriendList(this)
    }

    /**
     * get friend list success
     * @param result result
     */
    override fun getFriendListSuccess(result: FriendListResponse) {
        commonUseFragmentView.getFriendListAfter()
        if (result.data.isEmpty()) {
            commonUseFragmentView.getFriendListZero()
            return
        }
        commonUseFragmentView.getFriendListSuccess(result)
    }

    /**
     * get friend list failed
     * @param errorMessage error message
     */
    override fun getFriendListFailed(errorMessage: String?) {
        commonUseFragmentView.getFriendListAfter()
        commonUseFragmentView.getFriendListFailed(errorMessage)
    }

    /**
     * cancel request
     */
    fun cancelRequest() {
        homeModel.cancelHomeListRequest()
    }
}