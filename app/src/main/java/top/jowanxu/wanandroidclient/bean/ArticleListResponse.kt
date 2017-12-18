package top.jowanxu.wanandroidclient.bean

data class ArticleListResponse(var errorCode: Int,
                               var errorMsg: Any,
                               var data: Data) {
    data class Data(var offset: Int,
                    var size: Int,
                    var total: Int,
                    var pageCount: Int,
                    var curPage: Int,
                    var over: Boolean,
                    var datas: List<Datas>?) {
        data class Datas(var id: Int,
                         var title: String,
                         var chapterId: Int,
                         var chapterName: String,
                         var envelopePic: Any,
                         var link: String,
                         var author: String,
                         var origin: Any,
                         var publishTime: Long,
                         var zan: Any,
                         var desc: Any,
                         var visible: Int,
                         var niceDate: String,
                         var courseId: Int,
                         var collect: Boolean)
    }
}