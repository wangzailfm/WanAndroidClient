package top.jowanxu.wanandroidclient.ui.activity

import Constant
import android.content.Intent
import android.os.Bundle
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
import top.jowanxu.wanandroidclient.adapter.SearchAdapter
import top.jowanxu.wanandroidclient.base.BaseActivity
import top.jowanxu.wanandroidclient.bean.Datas
import top.jowanxu.wanandroidclient.bean.SearchListResponse
import top.jowanxu.wanandroidclient.presenter.SearchPresenterImpl
import top.jowanxu.wanandroidclient.view.SearchListView

/**
 * SearchActivity
 */
class SearchActivity : BaseActivity(), SearchListView {

    /**
     * Data List
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
    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter(this, datas)
    }
    /**
     * Search key
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
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener({
                searchKey?.let {
                    val page = searchAdapter.data.size / 20 + 1
                    searchPresenter.getSearchList(page, it)
                }
            }, recyclerView)
            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }
    }

    override fun onPause() {
        super.onPause()
        searchPresenter.cancelRequest()
        searchAdapter.setEnableLoadMore(false)
        searchAdapter.loadMoreComplete()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        // get SearchView
        searchView = menu.findItem(R.id.menuSearch).actionView as SearchView
        // init SearchView
        searchView.init(1920, false, onQueryTextListener = onQueryTextListener)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // back clickListener
        if (item.itemId == android.R.id.home) {
            // remove focus
            searchView.clearFocus()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getSearchListAfter() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getSearchListZero() {
        toast("未搜索到关键词相关文章")
    }

    override fun getSearchListSmall(result: SearchListResponse) {
        result.data.datas?.let {
            searchAdapter.run {
                replaceData(it)
                loadMoreEnd()
                loadMoreComplete()
                setEnableLoadMore(false)
            }
        }
    }

    override fun getSearchListSuccess(result: SearchListResponse) {
        result.data.datas?.let {
            searchAdapter.run {
                // 列表总数
                val total = result.data.total
                // 当前总数
                if (data.size >= total) {
                    loadMoreEnd()
                    return@let
                }
                if (swipeRefreshLayout.isRefreshing) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                loadMoreComplete()
                setEnableLoadMore(true)
            }
        }
    }

    override fun getSearchListFailed(errorMessage: String?) {
        searchAdapter.setEnableLoadMore(false)
        swipeRefreshLayout.isRefreshing = false
        searchAdapter.loadMoreFail()
        toast("获取数据失败")
    }
    /**
     * QueryListener
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
     * RefreshListener
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
     * ItemClickListener
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
     * ItemChildClickListener
     */
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, _, position ->
        if (datas.size != 0) {
            if (datas.size != 0) {
                Intent(this, TypeContentActivity::class.java).run {
                    putExtra(Constant.CONTENT_TARGET_KEY, true)
                    putExtra(Constant.CONTENT_TITLE_KEY, datas[position].chapterName)
                    putExtra(Constant.CONTENT_CID_KEY, datas[position].chapterId)
                    startActivity(this)
                }
            }
        }
    }
    /**
     * init SearchView
     */
    private fun SearchView.init(sMaxWidth: Int = 0, sIconified: Boolean = false, isClose: Boolean = false, onQueryTextListener: SearchView.OnQueryTextListener) = this.run {
        if (sMaxWidth != 0) {
            maxWidth = sMaxWidth
        }
        // false为展开
        isIconified = sIconified
        // 不关闭
        if (!isClose) {
            // 展开
            onActionViewExpanded()
        }
        // 搜索监听
        setOnQueryTextListener(onQueryTextListener)
    }
}