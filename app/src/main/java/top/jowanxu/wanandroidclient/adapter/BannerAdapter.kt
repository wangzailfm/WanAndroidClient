package top.jowanxu.wanandroidclient.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.bean.BannerResponse

class BannerAdapter(val context: Context, datas: MutableList<BannerResponse.Data>)
    : BaseQuickAdapter<BannerResponse.Data, BaseViewHolder>(R.layout.banner_item, datas) {
    override fun convert(helper: BaseViewHolder, item: BannerResponse.Data?) {
        item?.let {
            helper.setText(R.id.bannerTitle, it.title.trim())
            val imageView = helper.getView(R.id.bannerImage) as ImageView
            Glide.with(context).load(it.imagePath).placeholder(R.drawable.logo).into(imageView)
        }
    }
}