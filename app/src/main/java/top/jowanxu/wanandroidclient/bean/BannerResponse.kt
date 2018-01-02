package top.jowanxu.wanandroidclient.bean

data class BannerResponse(var errorCode: Int,
                          var errorMsg: String?,
                          var data: List<Data>?) {
    data class Data(var id: Int,
                    var url: String,
                    var imagePath: String,
                    var title: String,
                    var desc: String,
                    var isVisible: Int,
                    var order: Int,
                    var `type`: Int)
}