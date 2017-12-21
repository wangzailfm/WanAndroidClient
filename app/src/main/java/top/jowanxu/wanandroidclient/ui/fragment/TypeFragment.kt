package top.jowanxu.wanandroidclient.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_type.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.adapter.TypeAdapter
import top.jowanxu.wanandroidclient.bean.TreeListResponse
import top.jowanxu.wanandroidclient.presenter.TypeFragmentPresenterImpl
import top.jowanxu.wanandroidclient.view.TypeFragmentView

class TypeFragment : Fragment(), TypeFragmentView {
    /**
     * mainView
     */
    private var mainView: View? = null
    private var datas = mutableListOf<TreeListResponse.Data>()
    private val typeFragmentPresenter: TypeFragmentPresenterImpl by lazy {
        TypeFragmentPresenterImpl(this)
    }
    private val typeAdapter: TypeAdapter by lazy {
        TypeAdapter(activity, datas)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView =inflater?.inflate(R.layout.fragment_type, null)
        }
        return mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        typeSwipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }
        typeRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = typeAdapter
        }
        typeAdapter.run {
            bindToRecyclerView(typeRecyclerView)
            setEmptyView(R.layout.fragment_home_empty)
        }
        typeFragmentPresenter.getTypeTreeList()
    }

    override fun onPause() {
        super.onPause()
        typeFragmentPresenter.cancelRequest()
    }

    /**
     * scroll to top
     */
    fun smoothScrollToPosition() = recyclerView.smoothScrollToPosition(0)

    /**
     * get Type list after operation
     */
    override fun getTypeListAfter() {
        typeSwipeRefreshLayout.isRefreshing = false
    }

    /**
     * get Type list Success
     */
    override fun getTypeListSuccess(result: TreeListResponse) {
        result.data.let {
            if (typeSwipeRefreshLayout.isRefreshing) {
                typeAdapter.replaceData(it)
            } else {
                typeAdapter.addData(it)
            }
        }
    }

    /**
     * get Type list Failed
     */
    override fun getTypeListFailed(errorMessage: String?) {
        activity.toast("获取数据失败")
    }

    /**
     * get Type list data size equal zero
     */
    override fun getTypeListZero() {
        activity.toast("未搜索到关键词相关文章")
    }
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        typeSwipeRefreshLayout.isRefreshing = true
        datas.clear()
        typeFragmentPresenter.getTypeTreeList()
    }
}