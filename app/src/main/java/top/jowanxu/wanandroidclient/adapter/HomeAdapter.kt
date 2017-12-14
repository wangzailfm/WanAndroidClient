package top.jowanxu.wanandroidclient.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.bean.HomeListResponse.Data.Datas

class HomeAdapter(val context: Context, datas: MutableList<Datas>)
    : BaseQuickAdapter<Datas, BaseViewHolder>(R.layout.home_list_item, datas) {
    override fun convert(helper: BaseViewHolder?, item: Datas?) {
        helper?.let { holder ->
            item?.let {
                holder.setText(R.id.homeItemTitle, it.title)
                holder.setText(R.id.homeItemAuthor, "作者：${it.author}")
                holder.setText(R.id.homeItemType, "类别：${it.chapterName}")
                holder.setText(R.id.homeItemDate, it.niceDate)
            }
        }
    }
}