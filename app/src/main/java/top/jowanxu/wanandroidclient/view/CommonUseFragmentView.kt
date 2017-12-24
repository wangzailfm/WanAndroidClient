package top.jowanxu.wanandroidclient.view

import top.jowanxu.wanandroidclient.bean.FriendListResponse

/**
 * CommonUse Fragment View interface
 */
interface CommonUseFragmentView {
    /**
     * get Friend list after operation
     */
    fun getFriendListAfter()
    /**
     * get Friend list Success
     */
    fun getFriendListSuccess(result: FriendListResponse)
    /**
     * get Friend list Failed
     */
    fun getFriendListFailed(errorMessage: String?)
    /**
     * get Friend list data size equal zero
     */
    fun getFriendListZero()
}