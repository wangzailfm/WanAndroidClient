package top.jowanxu.wanandroidclient.bean

data class FriendListResponse(var errorCode: Int,
                              var errorMsg: String?,
                              var data: List<Data>?) {
    data class Data(var id: Int,
                    var name: String,
                    var link: String,
                    var visible: Int,
                    var order: Int,
                    var icon: Any)
}