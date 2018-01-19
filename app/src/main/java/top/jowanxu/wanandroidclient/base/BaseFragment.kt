package top.jowanxu.wanandroidclient.base

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

    protected var isFirst: Boolean = true

    /**
     * cancel request
     */
    protected abstract fun cancelRequest()

    override fun onDestroyView() {
        super.onDestroyView()
        cancelRequest()
    }
}