package top.jowanxu.wanandroidclient

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import top.jowanxu.wanandroidclient.adapter.HomeItemAdapter
import top.jowanxu.wanandroidclient.bean.HomeListModel
import top.jowanxu.wanandroidclient.bean.HomeListModel.Data.Datas
import top.jowanxu.wanandroidclient.presenter.HomePresenterImpl
import top.jowanxu.wanandroidclient.view.HomeView



class MainActivity : AppCompatActivity(), HomeView {

    private val mHomePresenter: HomePresenterImpl by lazy {
        HomePresenterImpl(this)
    }

    private var mDatas = mutableListOf<Datas>()

    private val mHomeAdapter: HomeItemAdapter by lazy {
        HomeItemAdapter(this, mDatas)
    }

    private var currentIndex = 0

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                return@OnNavigationItemSelectedListener when (item.itemId) {
                    R.id.navigation_home -> {
                        if (currentIndex == R.id.navigation_home) {
                            recyclerView.smoothScrollToPosition(0)
                        }
                        currentIndex = R.id.navigation_home
                        if (mDatas.size == 0) {
                            mHomePresenter.getHomeList()
                        }
                        true
                    }
                    R.id.navigation_dashboard -> {
                        currentIndex = R.id.navigation_dashboard
                        true
                    }
                    R.id.navigation_notifications -> {
                        currentIndex = R.id.navigation_notifications
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mHomeAdapter
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home
    }

    override fun getHomeListSuccess(result: HomeListModel) {
        result.data.datas?.let {
            mDatas.clear()
            mDatas.addAll(it)
            mHomeAdapter.notifyDataSetChanged()
        }
    }

    override fun getHomeListFailed(errorMessage: String?) {

    }

    fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
    }
}
