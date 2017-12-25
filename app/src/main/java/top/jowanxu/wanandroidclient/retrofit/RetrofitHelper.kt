
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.jowanxu.wanandroidclient.base.Preference
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
            // get response cookie
            addInterceptor {
                val request = it.request()
                val response = it.proceed(request)
                //set-cookie可能为多个
                if (!response.headers("set-cookie").isEmpty()) {
                    val cookies = response.headers("set-cookie")
                    val cookie = encodeCookie(cookies)
                    loge(TAG, "-----------$cookie")
                    saveCookie(request.url().toString(), request.url().host(), cookie)
                }
                response
            }
            // set request cookie
            addInterceptor {
                val request = it.request()
                val builder = request.newBuilder()
                val requestUrl = request.url().toString()
                val domain = request.url().host()
                if (!requestUrl.isEmpty() && !domain.isEmpty()) {
                    val spUrl: String by Preference(url, "")
                    val spDomain: String by Preference(domain, "")
                    var cookie = ""
                    if (!spUrl.isEmpty()) {
                        cookie = spUrl
                    } else if (!spDomain.isEmpty()) {
                        cookie = spDomain
                    } else {
                        cookie = ""
                    }
                    if (!cookie.isEmpty()) {
                        builder.addHeader("Cookie", cookie)
                    }
                }
                it.proceed(request)
            }
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

    private fun encodeCookie(cookies: List<String>): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies
                .map { cookie ->
                    cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() }
                .forEach {
                    it.filterNot { set.contains(it) }.forEach { set.add(it) }
                }

        val ite = set.iterator()
        while (ite.hasNext()) {
            val cookie = ite.next()
            sb.append(cookie).append(";")
        }

        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }

        return sb.toString()
    }

    private fun saveCookie(url: String?, domain: String?, cookies: String) {
        url ?: return
        var spUrl: String by Preference(url, cookies)
        spUrl = cookies
        domain ?: return
        var spDomain: String by Preference(domain, cookies)
        spDomain = cookies
    }
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

/**
 * login
 */
fun loginWanAndroid(username: String, password: String) = RetrofitHelper.retrofitService.loginWanAndroid(username, password)

/**
 * register
 */
fun registerWanAndroid(username: String, password: String, repassword: String) = RetrofitHelper.retrofitService.registerWanAndroid(username, password, repassword)

/**
 * Friend list call
 */
fun getFriendListCall() = RetrofitHelper.retrofitService.getFriendList()