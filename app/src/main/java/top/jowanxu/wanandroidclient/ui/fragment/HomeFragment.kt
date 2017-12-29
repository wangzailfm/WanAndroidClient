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
import kotlinx.android.synthetic.main.activity_search.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.adapter.HomeAdapter
import top.jowanxu.wanandroidclient.base.Preference
import top.jowanxu.wanandroidclient.bean.Datas
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.presenter.HomeFragmentPresenterImpl
import top.jowanxu.wanandroidclient.ui.activity.ContentActivity
import top.jowanxu.wanandroidclient.ui.activity.LoginActivity
import top.jowanxu.wanandroidclient.ui.activity.TypeContentActivity
import top.jowanxu.wanandroidclient.view.CollectArticleView
import top.jowanxu.wanandroidclient.view.HomeFragmentView

class HomeFragment : Fragment(), HomeFragmentView, CollectArticleView {
    /**
     * mainView
     */
    private var mainView: View? = null
    /**
     * Data List
     */
    private val datas = mutableListOf<Datas>()
    /**
     * presenter
     */
    private val homeFragmentPresenter: HomeFragmentPresenterImpl by lazy {
        HomeFragmentPresenterImpl(this, this)
    }
    /**
     * adapter
     */
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(activity, datas)
    }
    /**
     * check login for SharedPreferences
     */
    private val isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView = inflater.inflate(R.layout.fragment_home, container, false)
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
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@HomeFragment.onItemClickListener
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }
        homeFragmentPresenter.getHomeList()
    }

/*    override fun onPause() {
        super.onPause()
        homeFragmentPresenter.cancelRequest()
        homeAdapter.loadMoreComplete()
        swipeRefreshLayout.isRefreshing = false
    }*/

    /**
     * scroll to top
     */
    fun smoothScrollToPosition() = recyclerView.scrollToPosition(0)
    /**
     * refresh
     */
    fun refreshData() {
        swipeRefreshLayout.isRefreshing = true
        homeAdapter.setEnableLoadMore(false)
        homeFragmentPresenter.getHomeList()
    }

    override fun getHomeListZero() {
        activity.toast(getString(R.string.get_data_error))
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getHomeListSmall(result: HomeListResponse) {
        result.data.datas?.let {
            homeAdapter.run {
                replaceData(it)
                loadMoreComplete()
                loadMoreEnd()
                setEnableLoadMore(false)
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getHomeListSuccess(result: HomeListResponse) {
        result.data.datas?.let {
            homeAdapter.run {
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

    override fun getHomeListFailed(errorMessage: String?) {
        homeAdapter.setEnableLoadMore(false)
        homeAdapter.loadMoreFail()
        errorMessage?.let {
            activity.toast(it)
        } ?: let {
            activity.toast(getString(R.string.get_data_error))
        }
        swipeRefreshLayout.isRefreshing = false
    }

    /**
     * add article success
     * @param result HomeListResponse
     * @param isAdd true add, false remove
     */
    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        activity.toast(if (isAdd) activity.getString(R.string.bookmark_success) else activity.getString(R.string.bookmark_cancel_success))
    }

    /**
     * add article false
     * @param errorMessage error message
     * @param isAdd true add, false remove
     */
    override fun collectArticleFailed(errorMessage: String?, isAdd: Boolean) {
        activity.toast(if (isAdd) activity.getString(R.string.bookmark_failed, errorMessage) else activity.getString(R.string.bookmark_cancel_failed, errorMessage))
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshData()
    }
    /**
     * ItemClickListener
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
    /**
     * ItemChildClickListener
     */
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        if (datas.size != 0) {
            val data = datas[position]
            when (view.id) {
                R.id.homeItemType -> {
                    Intent(activity, TypeContentActivity::class.java).run {
                        putExtra(Constant.CONTENT_TARGET_KEY, true)
                        putExtra(Constant.CONTENT_TITLE_KEY, data.chapterName)
                        putExtra(Constant.CONTENT_CID_KEY, data.chapterId)
                        startActivity(this)
                    }
                }
                R.id.homeItemLike -> {
                    if (isLogin) {
                        val collect = data.collect
                        data.collect = !collect
                        homeAdapter.setData(position, data)
                        homeFragmentPresenter.collectArticle(data.id, !collect)
                    } else {
                        Intent(activity, LoginActivity::class.java).run {
                            startActivityForResult(this, Constant.MAIN_REQUEST_CODE)
                        }
                        activity.toast(getString(R.string.login_please_login))
                    }
                }
            }
        }
    }
    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = homeAdapter.data.size / 20 + 1
        homeFragmentPresenter.getHomeList(page)
    }
}