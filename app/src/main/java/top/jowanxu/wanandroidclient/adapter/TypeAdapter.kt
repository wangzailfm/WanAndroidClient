package top.jowanxu.wanandroidclient.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.bean.TreeListResponse

class TypeAdapter(val context: Context, datas: MutableList<TreeListResponse.Data>)
    : BaseQuickAdapter<TreeListResponse.Data, BaseViewHolder>(R.layout.type_list_item, datas) {
    override fun convert(helper: BaseViewHolder, item: TreeListResponse.Data?) {
        item?.let {
            helper.setText(R.id.typeItemFirst, it.name)
            it.children?.let { children ->
                helper.setText(R.id.typeItemSecond, children.joinToString("     ", transform = {
                    child -> child.name
                }))
            }
        }
    }
}