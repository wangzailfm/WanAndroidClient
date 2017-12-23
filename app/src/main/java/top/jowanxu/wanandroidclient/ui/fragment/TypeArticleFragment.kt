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
import kotlinx.android.synthetic.main.fragment_type_content.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.adapter.TypeArticleAdapter
import top.jowanxu.wanandroidclient.bean.ArticleListResponse
import top.jowanxu.wanandroidclient.bean.Datas
import top.jowanxu.wanandroidclient.presenter.TypeArticlePresenterImpl
import top.jowanxu.wanandroidclient.ui.activity.ContentActivity
import top.jowanxu.wanandroidclient.view.TypeArticleFragmentView

class TypeArticleFragment : Fragment(), TypeArticleFragmentView {
    /**
     * mainView
     */
    private var mainView: View? = null
    /**
     * Data List
     */
    private var datas = mutableListOf<Datas>()
    /**
     * presenter
     */
    private val typeArticlePresenter: TypeArticlePresenterImpl by lazy {
        TypeArticlePresenterImpl(this)
    }
    /**
     * adapter
     */
    private val typeArticleAdapter: TypeArticleAdapter by lazy {
        TypeArticleAdapter(activity, datas)
    }
    /**
     * type id
     */
    private var cid: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView = inflater.inflate(R.layout.fragment_type_content, container, false)
        }
        return mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cid = arguments.getInt(Constant.CONTENT_CID_KEY)
        tabSwipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        tabRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = typeArticleAdapter
        }
        typeArticleAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener, tabRecyclerView)
            onItemClickListener = this@TypeArticleFragment.onItemClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }
        typeArticlePresenter.getTypeArticleList(cid = cid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        typeArticleAdapter.loadMoreComplete()
        tabSwipeRefreshLayout.isRefreshing = false
    }

    /**
     * get Type Article list after operation
     */
    override fun getTypeArticleListAfter() {
        tabSwipeRefreshLayout.isRefreshing = false
    }

    /**
     * get Type Article list Success
     * @param result ArticleListResponse
     */
    override fun getTypeArticleListSuccess(result: ArticleListResponse) {
        result.data.datas?.let {
            typeArticleAdapter.run {
                // 列表总数
                val total = result.data.total
                // 当前总数
                if (data.size >= total) {
                    loadMoreEnd()
                    return@let
                }
                if (tabSwipeRefreshLayout.isRefreshing) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                loadMoreComplete()
                setEnableLoadMore(true)
            }
        }
    }

    /**
     * get Type Article list Failed
     * @param errorMessage error message
     */
    override fun getTypeArticleListFailed(errorMessage: String?) {
        typeArticleAdapter.setEnableLoadMore(false)
        typeArticleAdapter.loadMoreFail()
        activity.toast("获取数据失败")
    }

    /**
     * get Type Article list data size equal zero
     */
    override fun getTypeArticleListZero() {
        activity.toast("未搜索到关键词相关文章")
    }

    /**
     * get Type Article list data less than 20
     * @param result ArticleListResponse
     */
    override fun getTypeArticleListSmall(result: ArticleListResponse) {
        result.data.datas?.let {
            typeArticleAdapter.run {
                replaceData(it)
                loadMoreComplete()
                loadMoreEnd()
                setEnableLoadMore(false)
            }
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        tabSwipeRefreshLayout.isRefreshing = true
        datas.clear()
        typeArticleAdapter.setEnableLoadMore(false)
        typeArticlePresenter.getTypeArticleList(cid = cid)
    }
    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.size != 0) {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, datas[position].link)
                putExtra(Constant.CONTENT_TITLE_KEY, datas[position].title)
                startActivity(this)
            }
        }
    }
    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = typeArticleAdapter.data.size / 20 + 1
        typeArticlePresenter.getTypeArticleList(page, cid)
    }

    companion object {
        fun newInstance(cid: Int): TypeArticleFragment {
            val fragment = TypeArticleFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }
}