package top.jowanxu.wanandroidclient.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.bean.Datas

class TypeArticleAdapter(val context: Context, datas: MutableList<Datas>)
    : BaseQuickAdapter<Datas, BaseViewHolder>(R.layout.home_list_item, datas) {
    override fun convert(helper: BaseViewHolder, item: Datas?) {
        item?.let {
            @Suppress("DEPRECATION")
            helper.setText(R.id.homeItemTitle, it.title)
                    .setText(R.id.homeItemAuthor, it.author)
                    .setVisible(R.id.homeItemType, false)
                    .setText(R.id.homeItemDate, it.niceDate)
                    .setImageResource(R.id.homeItemLike, if (it.collect) R.drawable.ic_action_like else R.drawable.ic_action_no_like)
                    .addOnClickListener(R.id.homeItemLike)
        }
    }
}