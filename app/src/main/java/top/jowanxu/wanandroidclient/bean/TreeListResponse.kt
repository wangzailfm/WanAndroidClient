package top.jowanxu.wanandroidclient.bean

import java.io.Serializable

data class TreeListResponse(var errorCode: Int,
                            var errorMsg: String?,
                            var data: List<Data>) {
    data class Data(var id: Int,
                    var name: String,
                    var courseId: Int,
                    var parentChapterId: Int,
                    var order: Int,
                    var visible: Int,
                    var children: List<Children>?) : Serializable {
        data class Children(var id: Int,
                            var name: String,
                            var courseId: Int,
                            var parentChapterId: Int,
                            var order: Int,
                            var visible: Int,
                            var children: List<Children>?) : Serializable
    }
}