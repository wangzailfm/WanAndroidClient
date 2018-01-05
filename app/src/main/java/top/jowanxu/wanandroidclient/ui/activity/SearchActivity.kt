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
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.adapter.SearchAdapter
import top.jowanxu.wanandroidclient.base.BaseActivity
import top.jowanxu.wanandroidclient.base.Preference
import top.jowanxu.wanandroidclient.bean.Datas
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.presenter.SearchPresenterImpl
import top.jowanxu.wanandroidclient.view.CollectArticleView
import top.jowanxu.wanandroidclient.view.SearchListView

/**
 * SearchActivity
 */
class SearchActivity : BaseActivity(), SearchListView, CollectArticleView {

    /**
     * Data List
     */
    private val datas = mutableListOf<Datas>()
    /**
     * presenter
     */
    private val searchPresenter: SearchPresenterImpl by lazy {
        SearchPresenterImpl(this, this)
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
    /**
     * true search, false like
     */
    private var isSearch: Boolean = true
    /**
     * true like, false bookmark
     */
    private var isLike: Boolean = true
    /**
     * check login for SharedPreferences
     */
    private val isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    override fun setLayoutId(): Int = R.layout.activity_search

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolbar).init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let {
            isSearch = it.getBoolean(Constant.SEARCH_KEY, true)
            isLike = it.getBoolean(Constant.LIKE_KEY, true)
        }
        toolbar.run {
            title = if (isSearch) "" else if (isLike) getString(R.string.my_like) else getString(R.string.my_bookmark)
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
        } else {
            intent.extras?.let {
                searchKey = it.getString(Constant.CONTENT_TITLE_KEY, null)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isSearch) {
            menuInflater.inflate(R.menu.menu_search, menu)
            // get SearchView
            searchView = menu.findItem(R.id.menuSearch).actionView as SearchView
            // init SearchView
            searchView?.init(1920, false, onQueryTextListener = onQueryTextListener)
            searchKey?.let {
                searchView?.setQuery(it, true)
            }
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

    override fun getSearchListZero() {
        if (isSearch) {
            toast(getString(R.string.search_failed_not_article))
        } else {
            toast(getString(R.string.get_data_zero))
        }
        swipeRefreshLayout.isRefreshing = false
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
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getSearchListSuccess(result: HomeListResponse) {
        result.data.datas?.let {
            searchAdapter.run {
                // 列表总数
                val total = result.data.total
                // 当前总数
                if (result.data.offset >= total || data.size >= total) {
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
        swipeRefreshLayout.isRefreshing = false
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
        swipeRefreshLayout.isRefreshing = false
    }

    /**
     * add article success
     * @param result HomeListResponse
     * @param isAdd true add, false remove
     */
    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        toast(if (isAdd) getString(R.string.bookmark_success) else getString(R.string.bookmark_cancel_success))
    }

    /**
     * add article false
     * @param errorMessage error message
     * @param isAdd true add, false remove
     */
    override fun collectArticleFailed(errorMessage: String?, isAdd: Boolean) {
        toast(if (isAdd) getString(R.string.bookmark_failed, errorMessage) else getString(R.string.bookmark_cancel_failed, errorMessage))
    }

    /**
     * QueryListener
     */
    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                searchKey = it
                swipeRefreshLayout.isRefreshing = true
                searchAdapter.setEnableLoadMore(false)
                searchPresenter.getSearchList(k = it)
            } ?: let {
                swipeRefreshLayout.isRefreshing = false
                toast(getString(R.string.search_not_empty))
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
            searchAdapter.setEnableLoadMore(false)
            swipeRefreshLayout.isRefreshing = true
            searchPresenter.getLikeList()
            return@OnRefreshListener
        }
        searchKey?.let {
            searchAdapter.setEnableLoadMore(false)
            swipeRefreshLayout.isRefreshing = true
            searchPresenter.getSearchList(k = it)
        } ?: let {
            swipeRefreshLayout.isRefreshing = false
            toast(getString(R.string.search_not_empty))
        }
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
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener {
        _, view, position ->
        if (datas.size != 0) {
            val data = datas[position]
            when (view.id) {
                R.id.homeItemType -> {
                    data.chapterName ?: let {
                        toast("类别为空")
                        return@OnItemChildClickListener
                    }
                    Intent(this, TypeContentActivity::class.java).run {
                        putExtra(Constant.CONTENT_TARGET_KEY, true)
                        putExtra(Constant.CONTENT_TITLE_KEY, data.chapterName)
                        putExtra(Constant.CONTENT_CID_KEY, data.chapterId)
                        startActivity(this)
                    }
                }
                R.id.homeItemLike -> {
                    if (!isLogin) {
                        Intent(this, LoginActivity::class.java).run {
                            startActivity(this)
                        }
                        toast(getString(R.string.login_please_login))
                        return@OnItemChildClickListener
                    }
                    if (!isSearch) {
                        // delete data
                        searchAdapter.remove(position)
                        searchPresenter.collectArticle(data.id, false)
                    } else {
                        val collect = data.collect
                        data.collect = !collect
                        searchAdapter.setData(position, data)
                        searchPresenter.collectArticle(data.id, !collect)
                    }
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
        // false open
        isIconified = sIconified
        // not close
        if (!isClose) {
            // open
            onActionViewExpanded()
        }
        // search listener
        setOnQueryTextListener(onQueryTextListener)
    }
}