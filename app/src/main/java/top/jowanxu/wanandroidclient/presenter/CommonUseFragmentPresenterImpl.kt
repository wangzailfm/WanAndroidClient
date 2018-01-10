package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.FriendListResponse
import top.jowanxu.wanandroidclient.bean.HotKeyResponse
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
     */
    override fun getFriendListSuccess(bookmarkResult: FriendListResponse?, commonResult: FriendListResponse, hotResult: HotKeyResponse) {
        if (commonResult.errorCode != 0 || hotResult.errorCode != 0) {
            commonUseFragmentView.getFriendListFailed(commonResult.errorMsg)
            return
        }
        if (commonResult.data == null || commonResult.data == null) {
            commonUseFragmentView.getFriendListZero()
            return
        }
        if (commonResult.data?.size == 0 && hotResult.data?.size == 0) {
            commonUseFragmentView.getFriendListZero()
            return
        }
        commonUseFragmentView.getFriendListSuccess(bookmarkResult, commonResult, hotResult)
    }

    /**
     * get friend list failed
     * @param errorMessage error message
     */
    override fun getFriendListFailed(errorMessage: String?) {
        commonUseFragmentView.getFriendListFailed(errorMessage)
    }

    /**
     * cancel request
     */
    fun cancelRequest() {
        homeModel.cancelHomeListRequest()
    }
}