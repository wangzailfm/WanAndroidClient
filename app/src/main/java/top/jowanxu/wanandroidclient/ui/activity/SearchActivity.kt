package top.jowanxu.wanandroidclient.ui.activity

import Constant
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.app_bar_main.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.adapter.HomeAdapter
import top.jowanxu.wanandroidclient.base.BaseActivity
import top.jowanxu.wanandroidclient.bean.Datas
import top.jowanxu.wanandroidclient.bean.SearchListResponse
import top.jowanxu.wanandroidclient.presenter.SearchPresenterImpl
import top.jowanxu.wanandroidclient.view.SearchListView

/**
 * 搜索界面
 */
class SearchActivity : BaseActivity(), SearchListView {

    /**
     * 列表总数
     */
    private var total = 0
    /**
     * 当前列表总数
     */
    private var currentTotal = 0
    /**
     * 数据列表
     */
    private var datas = mutableListOf<Datas>()
    /**
     * presenter
     */
    private val searchPresenter: SearchPresenterImpl by lazy {
        SearchPresenterImpl(this)
    }
    /**
     * adapter
     */
    private val searchAdapter: HomeAdapter by lazy {
        HomeAdapter(this, datas)
    }
    /**
     * 搜索
     */
    private var searchKey: String? = null
    /**
     * SearView
     */
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        toolbar.run {
            title = ""
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }
        searchAdapter.run {
            setOnLoadMoreListener({
                searchKey?.let {
                    val page = searchAdapter.data.size / 20 + 1
                    searchPresenter.getSearchList(page, it)
                }
            }, recyclerView)
            onItemClickListener = this@SearchActivity.onItemClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = MenuItemCompat.getActionView(menu.findItem(R.id.menuSearch)) as SearchView
        searchView.init(1920, false, onQueryTextListener = onQueryTextListener)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            searchView.clearFocus()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getSearchListSuccess(result: SearchListResponse) {
        total = result.data.total
        if (total == 0) {
            toast("未搜索到关键词相关文章")
        }
        result.data.datas?.let {
            searchAdapter.run {
                currentTotal = data.size
                if (currentTotal >= total) {
                    loadMoreEnd()
                    return@let
                }
                if (swipeRefreshLayout.isRefreshing) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                currentTotal = data.size
                loadMoreComplete()
            }
        }
        searchAdapter.setEnableLoadMore(true)
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getSearchListFailed(errorMessage: String?) {
        searchAdapter.setEnableLoadMore(true)
        swipeRefreshLayout.isRefreshing = false
        searchAdapter.loadMoreFail()
    }

    /**
     * 搜索监听
     */
    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                searchKey = it
                swipeRefreshLayout.isRefreshing = true
                datas.clear()
                searchAdapter.setEnableLoadMore(false)
                searchPresenter.getSearchList(k = it)
            } ?: let {
                swipeRefreshLayout.isRefreshing = false
                toast("搜索关键字不能为空")
            }
            searchView.clearFocus()
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean = false
    }

    /**
     * 刷新监听
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        searchKey?.let {
            swipeRefreshLayout.isRefreshing = true
            datas.clear()
            searchAdapter.setEnableLoadMore(false)
            searchPresenter.getSearchList(k = it)
        } ?: let {
            swipeRefreshLayout.isRefreshing = false
            toast("搜索关键字不能为空")
        }
    }
    /**
     * 点击item监听
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener {
        _, _, position ->
        if (datas.size != 0) {
            Intent(this, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, datas[position].link)
                putExtra(Constant.CONTENT_TITLE_KEY, datas[position].title)
                startActivity(this)
            }
        }
    }
    /**
     * 初始化SearchView
     */
    private fun SearchView.init(sMaxWidth: Int = 0, sIconified: Boolean = false, isClose: Boolean = false, onQueryTextListener: SearchView.OnQueryTextListener) = this.run {
        if (sMaxWidth != 0) {
            maxWidth = sMaxWidth
        }
        // false为展开
        isIconified = sIconified
        // 不关闭
        if (!isClose) {
            onActionViewExpanded()
        }
        setOnQueryTextListener(onQueryTextListener)
    }
}