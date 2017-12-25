package top.jowanxu.wanandroidclient.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.bean.Datas

class HomeAdapter(val context: Context, datas: MutableList<Datas>)
    : BaseQuickAdapter<Datas, BaseViewHolder>(R.layout.home_list_item, datas) {
    override fun convert(helper: BaseViewHolder, item: Datas?) {
        item?.let {
            @Suppress("DEPRECATION")
            helper.setText(R.id.homeItemTitle, it.title)
                    .setText(R.id.homeItemAuthor, "作者：${it.author}")
                    .setText(R.id.homeItemType, "类别：${it.chapterName}")
                    .setText(R.id.homeItemDate, it.niceDate)
                    .addOnClickListener(R.id.homeItemType)
                    .addOnClickListener(R.id.homeItemLike)
                    .setTextColor(R.id.homeItemType, context.resources.getColor(R.color.colorPrimary))
                    .linkify(R.id.homeItemType)
        }
    }
}