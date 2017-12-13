
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import top.jowanxu.wanandroidclient.bean.HomeListModel

/**
 * Retrofit请求api
 */
interface RetrofitService {

    /**
     * 首页数据
     * @param page 页数
     */
    @GET("/article/list/{page}/json")
    fun getHomeList(
            @Path("page") page: Int
    ): Call<HomeListModel>

}