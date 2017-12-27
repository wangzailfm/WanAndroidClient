package top.jowanxu.wanandroidclient.ui.fragment

import Constant
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.fragment_common.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.adapter.CommonUseAdapter
import top.jowanxu.wanandroidclient.adapter.CommonUseTagAdapter
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
     * Data list
     */
    private val datas = mutableListOf<FriendListResponse.Data>()
    /**
     * presenter
     */
    private val commonUseFragmentPresenter: CommonUseFragmentPresenterImpl by lazy {
        CommonUseFragmentPresenterImpl(this)
    }
    /**
     * adapter
     */
    private val commonUseAdapter: CommonUseAdapter by lazy {
        CommonUseAdapter(activity, datas)
    }
    private lateinit var flowLayout: LinearLayout
    private lateinit var tagFlowLayout: TagFlowLayout
    private val tagDatas = mutableListOf<HotKeyResponse.Data>()
    private val commonUseTagAdapter: CommonUseTagAdapter by lazy {
        CommonUseTagAdapter(activity, tagDatas)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView = inflater.inflate(R.layout.fragment_common, container, false)
            flowLayout = LayoutInflater.from(activity).inflate(R.layout.common_hot, null) as LinearLayout
            tagFlowLayout = flowLayout.findViewById<TagFlowLayout>(R.id.hotFlowLayout)
        }
        return  mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tagFlowLayout.run {
            adapter = commonUseTagAdapter
            setOnTagClickListener(onTagClickListener)
        }
        commonSwipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        commonRecyclerView.run {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = commonUseAdapter
        }
        commonUseAdapter.run {
            bindToRecyclerView(commonRecyclerView)
            onItemClickListener = this@CommonUseFragment.onItemClickListener
            setEmptyView(R.layout.fragment_home_empty)
            commonUseAdapter.addHeaderView(flowLayout)
        }
        commonUseFragmentPresenter.getFriendList()
    }
    /**
     * scroll to top
     */
    fun smoothScrollToPosition() = commonRecyclerView.smoothScrollToPosition(0)

    /**
     * get Friend list after operation
     */
    override fun getFriendListAfter() {
        commonSwipeRefreshLayout.isRefreshing = false
    }

    /**
     * get Friend list Success
     */
    override fun getFriendListSuccess(result: FriendListResponse, hotResult: HotKeyResponse) {
        result.data.let {
            if (commonSwipeRefreshLayout.isRefreshing) {
                commonUseAdapter.replaceData(it)
            } else {
                commonUseAdapter.addData(it)
            }
        }
        hotResult.data?.let {
            tagDatas.clear()
            tagDatas.addAll(it)
            commonUseTagAdapter.notifyDataChanged()
        }
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
    }

    /**
     * get Friend list data size equal zero
     */
    override fun getFriendListZero() {
        activity.toast(getString(R.string.get_data_error))
    }
    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        commonSwipeRefreshLayout.isRefreshing = true
        datas.clear()
        commonUseAdapter.setEnableLoadMore(false)
        commonUseFragmentPresenter.getFriendList()
    }
    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener {
        _, _, position ->
        if (datas.size != 0) {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, datas[position].link)
                putExtra(Constant.CONTENT_TITLE_KEY, datas[position].name)
                startActivity(this)
            }
        }
    }

    private val onTagClickListener = TagFlowLayout.OnTagClickListener {
        _, position, _ ->
        if (tagDatas.size != 0) {
            Intent(activity, SearchActivity::class.java).run {
                putExtra(Constant.SEARCH_KEY, true)
                putExtra(Constant.CONTENT_TITLE_KEY, tagDatas[position].name)
                startActivityForResult(this, Constant.MAIN_LIKE_REQUEST_CODE)
            }
        }
        true
    }
}
