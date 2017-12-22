
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private const val TAG = "RetrofitHelper"
    private const val CONTENT_PRE = "OkHttp: "
    private const val CONNECT_TIMEOUT = 60L
    private const val READ_TIMEOUT = 10L

    val retrofitService: RetrofitService = RetrofitHelper.getService(Constant.REQUEST_BASE_URL, RetrofitService::class.java)

    /**
     * create Retrofit
     */
    private fun create(url: String):Retrofit {
        // okHttpClientBuilder
        val okHttpClientBuilder = OkHttpClient().newBuilder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            if (Constant.INTERCEPTOR_ENABLE) {
                //OkHttp进行添加拦截器loggingInterceptor
                addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    loge(TAG,  CONTENT_PRE + it)
                }).apply {
                    //日志显示级别
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }

        return RetrofitBuild(url = url,
                client = okHttpClientBuilder.build(),
                gsonFactory = GsonConverterFactory.create()).retrofit
    }

    /**
     * get ServiceApi
     */
    private fun <T> getService(url: String, service: Class<T>): T = create(url).create(service)

}

/**
 * create retrofit build
 */
class RetrofitBuild(url: String, client: OkHttpClient, gsonFactory: GsonConverterFactory) {
    val retrofit: Retrofit = Retrofit.Builder().apply {
        baseUrl(url)
        client(client)
        addConverterFactory(gsonFactory)
    }.build()
}

/**
 * Home list call
 */
fun getHomeListCall(page: Int = 0) = RetrofitHelper.retrofitService.getHomeList(page)

/**
 * Search list call
 */
fun getSearchListCall(page: Int = 0, k: String) = RetrofitHelper.retrofitService.getSearchList(page, k)

/**
 * Type tree list call
 */
fun getTypeTreeListCall() = RetrofitHelper.retrofitService.getTypeTreeList()

/**
 * Type second list call
 */
fun getArticleListCall(page: Int = 0, cid: Int) = RetrofitHelper.retrofitService.getArticleList(page, cid)