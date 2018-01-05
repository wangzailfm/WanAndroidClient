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
import android.widget.LinearLayout
import com.zhy.view.flowlayout.TagFlowLayout
import inflater
import kotlinx.android.synthetic.main.fragment_bookmark.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.adapter.CommonAdapter
import top.jowanxu.wanandroidclient.adapter.CommonUseTagAdapter
import top.jowanxu.wanandroidclient.bean.FriendListResponse
import top.jowanxu.wanandroidclient.presenter.BookmarkFragmentPresenterImpl
import top.jowanxu.wanandroidclient.ui.activity.ContentActivity
import top.jowanxu.wanandroidclient.view.BookmarkFragmentView

class BookmarkFragment : Fragment(), BookmarkFragmentView {

    /**
     * mainView
     */
    private var mainView: View? = null
    /**
     * bookmark data list
     */
    private val datas = mutableListOf<FriendListResponse.Data>()
    /**
     * presenter
     */
    private val bookmarkFragmentPresenter: BookmarkFragmentPresenterImpl by lazy {
        BookmarkFragmentPresenterImpl(this)
    }
    /**
     * common adapter
     */
    private val bookmarkAdapter: CommonAdapter by lazy {
        CommonAdapter(activity, datas)
    }
    /**
     * bookmark layout
    */
    private lateinit var flowLayout: LinearLayout
    /**
     * bookmark tag flowLayout
     */
    private lateinit var bookmarkTagFlowLayout: TagFlowLayout
    /**
     * bookmark tag data
     */
    private val bookmarkDatas = mutableListOf<FriendListResponse.Data>()
    /**
     * bookmark tag adapter
     */
    private val bookmarkTagAdapter: CommonUseTagAdapter by lazy {
        CommonUseTagAdapter(activity, bookmarkDatas)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView = inflater.inflate(R.layout.fragment_bookmark, container, false)
            flowLayout = activity.inflater(R.layout.bookmark_head) as LinearLayout
            bookmarkTagFlowLayout = flowLayout.findViewById<TagFlowLayout>(R.id.bookmarkFlowLayout)
        }
        return  mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bookmarkTagFlowLayout.run {
            adapter = bookmarkTagAdapter
            setOnTagClickListener(onCommonUseTagClickListener)
        }
        bookmarkSwipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        bookmarkRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = bookmarkAdapter
        }
        bookmarkAdapter.run {
            bindToRecyclerView(bookmarkRecyclerView)
            addHeaderView(flowLayout)
        }
        bookmarkFragmentPresenter.getFriendList()
    }

    /**
     * get Friend list Success
     */
    override fun getFriendListSuccess(result: FriendListResponse) {
        result.data.let {
            bookmarkDatas.clear()
            bookmarkDatas.addAll(it)
            bookmarkTagAdapter.notifyDataChanged()
        }
        bookmarkSwipeRefreshLayout.isRefreshing = false
    }

    /**
     * get Friend list Failed
     */
    override fun getFriendListFailed(errorMessage: String?) {
        errorMessage?.let {
            activity.toast(it)
        } ?: let {
            activity.toast(getString(R.string.get_data_error))
        }
        bookmarkSwipeRefreshLayout.isRefreshing = false
    }

    /**
     * get Friend list data size equal zero
     */
    override fun getFriendListZero() {
        activity.toast(getString(R.string.get_data_zero))
        bookmarkSwipeRefreshLayout.isRefreshing = false
    }
    /**
     * refresh
     */
    fun refreshData() {
        bookmarkSwipeRefreshLayout.isRefreshing = true
        bookmarkFragmentPresenter.getFriendList()
    }
    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshData()
    }

    /**
     * onCommonUseTagClickListener
     */
    private val onCommonUseTagClickListener = TagFlowLayout.OnTagClickListener {
        _, position, _ ->
        if (bookmarkDatas.size != 0) {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, bookmarkDatas[position].link)
                putExtra(Constant.CONTENT_ID_KEY, bookmarkDatas[position].id)
                putExtra(Constant.CONTENT_TITLE_KEY, bookmarkDatas[position].name)
                startActivity(this)
            }
        }
        true
    }
}