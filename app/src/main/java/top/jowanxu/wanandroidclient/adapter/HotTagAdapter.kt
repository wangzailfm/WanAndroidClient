package top.jowanxu.wanandroidclient.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import getRandomColor
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.bean.HotKeyResponse

class HotTagAdapter(context: Context, datas: List<HotKeyResponse.Data>)
    : TagAdapter<HotKeyResponse.Data>(datas) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(parent: FlowLayout, position: Int, data: HotKeyResponse.Data): View {
        return (inflater.inflate(R.layout.common_hot_item, parent, false) as TextView).apply {
            text = data.name
            val parseColor = try {
                Color.parseColor(getRandomColor())
            } catch (_: Exception) {
                @Suppress("DEPRECATION")
                context.resources.getColor(R.color.colorAccent)
            }
            setTextColor(parseColor)
        }
    }
}