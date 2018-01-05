package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.FriendListResponse
import top.jowanxu.wanandroidclient.model.HomeModel
import top.jowanxu.wanandroidclient.model.HomeModelImpl
import top.jowanxu.wanandroidclient.view.BookmarkFragmentView

class BookmarkFragmentPresenterImpl(private val bookmarkFragmentView: BookmarkFragmentView) : HomePresenter.OnBookmarkListListener {

    private val homeModel: HomeModel = HomeModelImpl()
    /**
     * get friend tree list
     */
    override fun getFriendList() {
        homeModel.getBookmarkList(this)
    }

    /**
     * get friend list success
     * @param result result
     */
    override fun getFriendListSuccess(result: FriendListResponse) {
        if (result.errorCode != 0) {
            bookmarkFragmentView.getFriendListFailed(result.errorMsg)
            return
        }
        if (result.data.isEmpty()) {
            bookmarkFragmentView.getFriendListZero()
            return
        }
        bookmarkFragmentView.getFriendListSuccess(result)
    }

    /**
     * get friend list failed
     * @param errorMessage error message
     */
    override fun getFriendListFailed(errorMessage: String?) {
        bookmarkFragmentView.getFriendListFailed(errorMessage)
    }

    /**
     * cancel request
     */
    fun cancelRequest() {
        homeModel.cancelHomeListRequest()
    }
}