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
import top.jowanxu.wanandroidclient.bean.HomeListResponse
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
    private var searchView: SearchView? = null
    private var isSearch: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        intent.extras?.let {
            isSearch = it.getBoolean("search", true)
        }
        toolbar.run {
            title = if (isSearch) "" else getString(R.string.my_like)
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
                val page = searchAdapter.data.size / 20 + 1
                if (!isSearch) {
                    searchPresenter.getLikeList()
                } else {
                    searchKey?.let {
                        searchPresenter.getSearchList(page, it)
                    }
                }
            }, recyclerView)
            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }
        if (!isSearch) {
            searchPresenter.getLikeList()
        }
    }

    override fun onPause() {
        super.onPause()
        searchPresenter.cancelRequest()
        searchAdapter.setEnableLoadMore(false)
        searchAdapter.loadMoreComplete()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isSearch) {
            menuInflater.inflate(R.menu.menu_search, menu)
            // get SearchView
            searchView = menu.findItem(R.id.menuSearch).actionView as SearchView
            // init SearchView
            searchView?.init(1920, false, onQueryTextListener = onQueryTextListener)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // back clickListener
        if (item.itemId == android.R.id.home) {
            // remove focus
            searchView?.clearFocus()
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

    override fun getSearchListSmall(result: HomeListResponse) {
        result.data.datas?.let {
            searchAdapter.run {
                replaceData(it)
                loadMoreEnd()
                loadMoreComplete()
                setEnableLoadMore(false)
            }
        }
    }

    override fun getSearchListSuccess(result: HomeListResponse) {
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
        errorMessage?.let {
            toast(it)
        } ?: let {
            toast(getString(R.string.get_data_error))
        }
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
            searchView?.clearFocus()
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean = false
    }
    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        if (!isSearch) {
            swipeRefreshLayout.isRefreshing = true
            datas.clear()
            searchAdapter.setEnableLoadMore(false)
            searchPresenter.getLikeList()
            return@OnRefreshListener
        }
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
     * get Home list after operation
     */
    override fun getLikeListAfter() {
        getSearchListAfter()
    }

    /**
     * get Home list Success
     */
    override fun getLikeListSuccess(result: HomeListResponse) {
        getSearchListSuccess(result)
    }

    /**
     * get Home list Failed
     */
    override fun getLikeListFailed(errorMessage: String?) {
        getSearchListFailed(errorMessage)
    }

    /**
     * get Home list data size equal zero
     */
    override fun getLikeListZero() {
        getSearchListZero()
    }

    /**
     * get Home list data less than 20
     */
    override fun getLikeListSmall(result: HomeListResponse) {
        getSearchListSmall(result)
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