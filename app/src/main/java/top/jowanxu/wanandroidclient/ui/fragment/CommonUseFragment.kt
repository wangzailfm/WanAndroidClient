package top.jowanxu.wanandroidclient.ui.fragment

import Constant
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_common.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.adapter.CommonUseAdapter
import top.jowanxu.wanandroidclient.bean.FriendListResponse
import top.jowanxu.wanandroidclient.presenter.CommonUseFragmentPresenterImpl
import top.jowanxu.wanandroidclient.ui.activity.ContentActivity
import top.jowanxu.wanandroidclient.view.CommonUseFragmentView

class CommonUseFragment : Fragment(), CommonUseFragmentView {

    /**
     * mainView
     */
    private var mainView: View? = null
    /**
     * Data list
     */
    private var datas = mutableListOf<FriendListResponse.Data>()
    /**
     * presenter
     */
    private val commonUseFragmentPresenter: CommonUseFragmentPresenterImpl by lazy {
        CommonUseFragmentPresenterImpl(this)
    }
    /**
     * adapter
     */
    private val commonUseAdapter: CommonUseAdapter by lazy {
        CommonUseAdapter(activity, datas)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView = inflater.inflate(R.layout.fragment_common, container, false)
        }
        return  mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        commonSwipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        commonRecyclerView.run {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = commonUseAdapter
        }
        commonUseAdapter.run {
            bindToRecyclerView(commonRecyclerView)
            onItemClickListener = this@CommonUseFragment.onItemClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }
        commonUseFragmentPresenter.getFriendList()
    }
    /**
     * scroll to top
     */
    fun smoothScrollToPosition() = commonRecyclerView.smoothScrollToPosition(0)

    /**
     * get Friend list after operation
     */
    override fun getFriendListAfter() {
        commonSwipeRefreshLayout.isRefreshing = false
    }

    /**
     * get Friend list Success
     */
    override fun getFriendListSuccess(result: FriendListResponse) {
        result.data.let {
            if (commonSwipeRefreshLayout.isRefreshing) {
                commonUseAdapter.replaceData(it)
            } else {
                commonUseAdapter.addData(it)
            }
        }
    }

    /**
     * get Friend list Failed
     */
    override fun getFriendListFailed(errorMessage: String?) {
        activity.toast("获取数据失败")
    }

    /**
     * get Friend list data size equal zero
     */
    override fun getFriendListZero() {
        activity.toast("获取数据失败")
    }
    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        commonSwipeRefreshLayout.isRefreshing = true
        datas.clear()
        commonUseAdapter.setEnableLoadMore(false)
        commonUseFragmentPresenter.getFriendList()
    }
    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener {
        _, _, position ->
        if (datas.size != 0) {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, datas[position].link)
                putExtra(Constant.CONTENT_TITLE_KEY, datas[position].name)
                startActivity(this)
            }
        }
    }
}
