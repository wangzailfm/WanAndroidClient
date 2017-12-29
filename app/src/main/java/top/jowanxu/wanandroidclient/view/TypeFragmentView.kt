package top.jowanxu.wanandroidclient.view

import top.jowanxu.wanandroidclient.bean.TreeListResponse

/**
 * Type Fragment View interface
 */
interface TypeFragmentView {
    /**
     * get Type list Success
     */
    fun getTypeListSuccess(result: TreeListResponse)
    /**
     * get Type list Failed
     */
    fun getTypeListFailed(errorMessage: String?)
    /**
     * get Type list data size equal zero
     */
    fun getTypeListZero()
}