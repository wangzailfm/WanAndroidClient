package top.jowanxu.wanandroidclient.ui.fragment

import Constant
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.adapter.HomeAdapter
import top.jowanxu.wanandroidclient.bean.Datas
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.presenter.HomePresenterImpl
import top.jowanxu.wanandroidclient.ui.activity.ContentActivity
import top.jowanxu.wanandroidclient.view.HomeView

class HomeFragment : Fragment(), HomeView {

    /**
     * 列表总数
     */
    private var total = 0
    /**
     * 当前列表总数
     */
    private var currentTotal = 0

    private var mainView: View? = null

    /**
     * 数据列表
     */
    private var datas = mutableListOf<Datas>()
    /**
     * presenter
     */
    private val homePresenter: HomePresenterImpl by lazy {
        HomePresenterImpl(this)
    }
    /**
     * adapter
     */
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(activity, datas)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView =inflater?.inflate(R.layout.fragment_home, null)
        }
        return mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = homeAdapter
        }
        homeAdapter.run {
            setOnLoadMoreListener({
                val page = homeAdapter.data.size / 20 + 1
                homePresenter.getHomeList(page)
            }, recyclerView)
            onItemClickListener = this@HomeFragment.onItemClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }
        homePresenter.getHomeList()
    }

    override fun onPause() {
        super.onPause()
        homePresenter.cancelRequest()
    }

    /**
     * 滚动到顶部
     */
    fun smoothScrollToPosition() = recyclerView.smoothScrollToPosition(0)

    override fun getHomeListSuccess(result: HomeListResponse) {
        total = result.data.total
        if (total == 0) {
            activity.toast("未搜索到关键词相关文章")
        }
        result.data.datas?.let {
            currentTotal = homeAdapter.data.size
            if (currentTotal >= total) {
                homeAdapter.loadMoreEnd()
                return@let
            }
            if (swipeRefreshLayout.isRefreshing) {
                homeAdapter.replaceData(it)
            } else {
                homeAdapter.addData(it)
            }
            currentTotal = homeAdapter.data.size
            homeAdapter.loadMoreComplete()
        }
        homeAdapter.setEnableLoadMore(true)
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getHomeListFailed(errorMessage: String?) {
        homeAdapter.setEnableLoadMore(true)
        swipeRefreshLayout.isRefreshing = false
        homeAdapter.loadMoreFail()
    }

    /**
     * 刷新监听
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        swipeRefreshLayout.isRefreshing = true
        datas.clear()
        homeAdapter.setEnableLoadMore(false)
        homePresenter.getHomeList()
    }

    /**
     * 点击item监听
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener {
        _, _, position ->
        if (datas.size != 0) {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, datas[position].link)
                putExtra(Constant.CONTENT_TITLE_KEY, datas[position].title)
                startActivity(this)
            }
        }
    }
}