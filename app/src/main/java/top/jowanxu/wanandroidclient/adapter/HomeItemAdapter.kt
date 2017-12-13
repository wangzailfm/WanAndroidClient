package top.jowanxu.wanandroidclient.adapter

import android.content.Context
import com.wangzai.rvadapter.adapter.DelegateItemAdapter
import top.jowanxu.wanandroidclient.bean.HomeListModel.Data.Datas
import top.jowanxu.wanandroidclient.delegate.HomeItemDelegate

class HomeItemAdapter(mContext: Context, mDatas: List<Datas>)
    : DelegateItemAdapter<Datas>(mContext, mDatas) {
    init {
        addItemViewDelegate(HomeItemDelegate())
    }
}