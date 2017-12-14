package top.jowanxu.wanandroidclient

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_main.*
import toast
import top.jowanxu.wanandroidclient.adapter.HomeAdapter
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.bean.HomeListResponse.Data.Datas
import top.jowanxu.wanandroidclient.presenter.HomePresenterImpl
import top.jowanxu.wanandroidclient.view.HomeView

/**
 * 主界面
 */
class MainActivity : AppCompatActivity(), HomeView {

    /**
     * 列表总数
     */
    private var total = 0
    /**
     * 当前列表总数
     */
    private var currentTotal = 0
    /**
     * 当前下标
     */
    private var currentIndex = R.id.navigation_home
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
        HomeAdapter(this, datas)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = homeAdapter
        }
        homeAdapter.run {
            setOnLoadMoreListener({
                val page = homeAdapter.data.size / 20 + 1
                homePresenter.getHomeList(page)
            }, recyclerView)
            onItemClickListener = this@MainActivity.onItemClickListener
        }
        navigation.run {
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_home
        }
    }

    override fun onPause() {
        super.onPause()
        homePresenter.cancelRequest()
    }

    override fun getHomeListSuccess(result: HomeListResponse) {
        total = result.data.total
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
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                return@OnNavigationItemSelectedListener when (item.itemId) {
                    R.id.navigation_home -> {
                        if (currentIndex == R.id.navigation_home) {
                            if (!recyclerView.canScrollVertically(-1)) {
                                swipeRefreshLayout.isRefreshing = true
                                homeAdapter.setEnableLoadMore(false)
                                homePresenter.getHomeList()
                            } else {
                                recyclerView.smoothScrollToPosition(0)
                            }
                        }
                        currentIndex = R.id.navigation_home
                        true
                    }
                    R.id.navigation_dashboard -> {
                        currentIndex = R.id.navigation_dashboard
                        true
                    }
                    R.id.navigation_notifications -> {
                        currentIndex = R.id.navigation_notifications
                        true
                    }
                    else -> {
                        false
                    }
                }
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
        toast("ItemClick: $position")
    }
}
