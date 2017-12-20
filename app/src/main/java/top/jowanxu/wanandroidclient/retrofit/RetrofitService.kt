import retrofit2.Call
import retrofit2.http.*
import top.jowanxu.wanandroidclient.bean.*

/**
 * Retrofit请求api
 */
interface RetrofitService {

    /**
     * 首页数据
     * <p>
     *     http://www.wanandroid.com/article/list/0/json
     * @param page 页数
     */
    @GET("/article/list/{page}/json")
    fun getHomeList(
            @Path("page") page: Int
    ): Call<HomeListResponse>

    /**
     * 知识体系
     * <p>
     *     http://www.wanandroid.com/tree/json
     */
    @GET("/tree/json")
    fun getTreeList(): Call<TreeListResponse>

    /**
     * 知识体系下的文章
     * <p>
     *     http://www.wanandroid.com/article/list/0/json?cid=168
     * @param page
     * @param cid
     */
    @GET("/article/list/{page}/json")
    fun getArticleList(@Path("page") page: Int,
                       @Query("cid") cid: Int
    ): Call<ArticleListResponse>

    /**
     * 常用网站
     * <p>
     *     http://www.wanandroid.com/friend/json
     */
    @GET("/friend/json")
    fun getFriendList(): Call<FriendListResponse>

    /**
     * 搜索
     * <p>
     *     Post
     * <p>
     *     http://www.wanandroid.com/article/query/0/json
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    fun getSearchList(@Path("page") page: Int,
                      @Field("k") k: String
    ): Call<SearchListResponse>

}