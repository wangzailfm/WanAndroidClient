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
import android.widget.TextView
import com.zhy.view.flowlayout.TagFlowLayout
import inflater
import kotlinx.android.synthetic.main.fragment_common.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.adapter.CommonAdapter
import top.jowanxu.wanandroidclient.adapter.CommonUseTagAdapter
import top.jowanxu.wanandroidclient.adapter.HotTagAdapter
import top.jowanxu.wanandroidclient.bean.FriendListResponse
import top.jowanxu.wanandroidclient.bean.HotKeyResponse
import top.jowanxu.wanandroidclient.presenter.CommonUseFragmentPresenterImpl
import top.jowanxu.wanandroidclient.ui.activity.ContentActivity
import top.jowanxu.wanandroidclient.ui.activity.SearchActivity
import top.jowanxu.wanandroidclient.view.CommonUseFragmentView

class CommonUseFragment : Fragment(), CommonUseFragmentView {

    /**
     * mainView
     */
    private var mainView: View? = null
    /**
     * common data list
     */
    private val datas = mutableListOf<FriendListResponse.Data>()
    /**
     * presenter
     */
    private val commonUseFragmentPresenter: CommonUseFragmentPresenterImpl by lazy {
        CommonUseFragmentPresenterImpl(this)
    }
    /**
     * common adapter
     */
    private val commonAdapter: CommonAdapter by lazy {
        CommonAdapter(activity, datas)
    }
    /**
     * common use and hot key layout
     */
    private lateinit var flowLayout: LinearLayout
    /**
     * hot key tag flowLayout
     */
    private lateinit var hotTagFlowLayout: TagFlowLayout
    /**
     * hot key tag data
     */
    private val hotTagDatas = mutableListOf<HotKeyResponse.Data>()
    /**
     * hot key tag adapter
     */
    private val hotTagAdapter: HotTagAdapter by lazy {
        HotTagAdapter(activity, hotTagDatas)
    }
    /**
     * common use tag flowLayout
     */
    private lateinit var commonUseTagFlowLayout: TagFlowLayout
    /**
     * common use tag data
     */
    private val commonUseDatas = mutableListOf<FriendListResponse.Data>()
    /**
     * common use tag adapter
     */
    private val commonUseTagAdapter: CommonUseTagAdapter by lazy {
        CommonUseTagAdapter(activity, commonUseDatas)
    }
    private lateinit var bookmarkTitle: TextView
    /**
     * bookmark tag flowLayout
     */
    private lateinit var bookmarkTagFlowLayout: TagFlowLayout
    /**
     * bookmark tag data
     */
    private val bookmarkUseDatas = mutableListOf<FriendListResponse.Data>()
    /**
     * bookmark tag adapter
     */
    private val bookmarkTagAdapter: CommonUseTagAdapter by lazy {
        CommonUseTagAdapter(activity, bookmarkUseDatas)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView = inflater.inflate(R.layout.fragment_common, container, false)
            flowLayout = activity.inflater(R.layout.common_hot) as LinearLayout
            hotTagFlowLayout = flowLayout.findViewById<TagFlowLayout>(R.id.hotFlowLayout)
            commonUseTagFlowLayout = flowLayout.findViewById<TagFlowLayout>(R.id.commonUseFlowLayout)
            bookmarkTitle = flowLayout.findViewById<TextView>(R.id.bookmarkTitle)
            bookmarkTagFlowLayout = flowLayout.findViewById<TagFlowLayout>(R.id.bookmarkFlowLayout)
        }
        return  mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bookmarkTagFlowLayout.run {
            adapter = bookmarkTagAdapter
            setOnTagClickListener(onBookmarkTagClickListener)
        }
        hotTagFlowLayout.run {
            adapter = hotTagAdapter
            setOnTagClickListener(onHotTagClickListener)
        }
        commonUseTagFlowLayout.run {
            adapter = commonUseTagAdapter
            setOnTagClickListener(onCommonUseTagClickListener)
        }
        commonSwipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        commonRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = commonAdapter
        }
        commonAdapter.run {
            bindToRecyclerView(commonRecyclerView)
            addHeaderView(flowLayout)
        }
        commonUseFragmentPresenter.getFriendList()
    }

    /**
     * get Friend list Success
     */
    override fun getFriendListSuccess(bookmarkResult: FriendListResponse?, commonResult: FriendListResponse, hotResult: HotKeyResponse) {
        bookmarkResult?.let {
            it.data?.let {
                bookmarkTitle.visibility = View.VISIBLE
                bookmarkTagFlowLayout.visibility = View.VISIBLE
                bookmarkUseDatas.clear()
                bookmarkUseDatas.addAll(it)
                bookmarkTagAdapter.notifyDataChanged()
            }?: let {
                bookmarkTitle.visibility = View.GONE
                bookmarkTagFlowLayout.visibility = View.GONE
            }
        }
        commonResult.data?.let {
            commonUseDatas.clear()
            commonUseDatas.addAll(it)
            commonUseTagAdapter.notifyDataChanged()
        }
        hotResult.data?.let {
            hotTagDatas.clear()
            hotTagDatas.addAll(it)
            hotTagAdapter.notifyDataChanged()
        }
        commonSwipeRefreshLayout.isRefreshing = false
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
        commonSwipeRefreshLayout.isRefreshing = false
    }

    /**
     * get Friend list data size equal zero
     */
    override fun getFriendListZero() {
        activity.toast(getString(R.string.get_data_zero))
        commonSwipeRefreshLayout.isRefreshing = false
    }
    /**
     * refresh
     */
    fun refreshData() {
        commonSwipeRefreshLayout.isRefreshing = true
        commonUseFragmentPresenter.getFriendList()
    }
    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshData()
    }

    /**
     * onBookmarkTagClickListener
     */
    private val onBookmarkTagClickListener = TagFlowLayout.OnTagClickListener {
        _, position, _ ->
        if (bookmarkUseDatas.size != 0) {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, bookmarkUseDatas[position].link)
                putExtra(Constant.CONTENT_ID_KEY, bookmarkUseDatas[position].id)
                putExtra(Constant.CONTENT_TITLE_KEY, bookmarkUseDatas[position].name)
                startActivity(this)
            }
        }
        true
    }

    /**
     * onCommonUseTagClickListener
     */
    private val onCommonUseTagClickListener = TagFlowLayout.OnTagClickListener {
        _, position, _ ->
        if (commonUseDatas.size != 0) {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, commonUseDatas[position].link)
                putExtra(Constant.CONTENT_ID_KEY, commonUseDatas[position].id)
                putExtra(Constant.CONTENT_TITLE_KEY, commonUseDatas[position].name)
                startActivity(this)
            }
        }
        true
    }
    /**
     * hot onCommonUseTagClickListener
     */
    private val onHotTagClickListener = TagFlowLayout.OnTagClickListener {
        _, position, _ ->
        if (hotTagDatas.size != 0) {
            Intent(activity, SearchActivity::class.java).run {
                putExtra(Constant.SEARCH_KEY, true)
                putExtra(Constant.CONTENT_TITLE_KEY, hotTagDatas[position].name)
                startActivityForResult(this, Constant.MAIN_LIKE_REQUEST_CODE)
            }
        }
        true
    }
}
