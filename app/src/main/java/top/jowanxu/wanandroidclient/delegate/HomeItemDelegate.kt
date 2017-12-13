package top.jowanxu.wanandroidclient.delegate

import android.annotation.SuppressLint
import android.content.Context
import com.wangzai.rvadapter.base.DelegateType
import com.wangzai.rvadapter.base.ViewHolder
import kotlinx.android.synthetic.main.home_list_item.view.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.bean.HomeListModel.Data.Datas

class HomeItemDelegate : DelegateType<Datas> {
    override val itemViewLayoutId: Int
        get() = R.layout.home_list_item

    override fun isItemViewType(item: Datas, position: Int): Boolean = true

    @SuppressLint("SetTextI18n")
    override fun convert(context: Context, holder: ViewHolder, item: Datas, position: Int) {
        with(holder.itemView) {
            homeItemTitle.text = item.title
            homeItemAuthor.text = "作者：${item.author}"
            homeItemType.text = "类别：${item.chapterName}"
            homeItemDate.text = item.niceDate
            setOnClickListener {
                context.toast("SingleItemDelegate")
            }
        }
    }
}