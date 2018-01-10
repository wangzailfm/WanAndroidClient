
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.jowanxu.wanandroidclient.base.Preference
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private const val TAG = "RetrofitHelper"
    private const val CONTENT_PRE = "OkHttp: "
    private const val SAVE_USER_LOGIN_KEY = "user/login"
    private const val SAVE_USER_REGISTER_KEY = "user/register"
    private const val SET_COOKIE_KEY = "set-cookie"
    private const val COOKIE_NAME = "Cookie"
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
                val requestUrl = request.url().toString()
                val domain = request.url().host()
                // set-cookie maybe has multi, login to save cookie
                if ((requestUrl.contains(SAVE_USER_LOGIN_KEY) || requestUrl.contains(SAVE_USER_REGISTER_KEY))
                        && !response.headers(SET_COOKIE_KEY).isEmpty()) {
                    val cookies = response.headers(SET_COOKIE_KEY)
                    val cookie = encodeCookie(cookies)
                    saveCookie(requestUrl, domain, cookie)
                }
                response
            }
            // set request cookie
            addInterceptor {
                val request = it.request()
                val builder = request.newBuilder()
                val domain = request.url().host()
                // get domain cookie
                if (domain.isNotEmpty()) {
                    val spDomain: String by Preference(domain, "")
                    val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
                    if (cookie.isNotEmpty()) {
                        builder.addHeader(COOKIE_NAME, cookie)
                    }
                }
                it.proceed(builder.build())
            }
            if (Constant.INTERCEPTOR_ENABLE) {
                // loggingInterceptor
                addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    loge(TAG,  CONTENT_PRE + it)
                }).apply {
                    // log level
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }

        return RetrofitBuild(url = url,
                client = okHttpClientBuilder.build(),
                gsonFactory = GsonConverterFactory.create(),
                coroutineCallAdapterFactory = CoroutineCallAdapterFactory())
                .retrofit
    }

    /**
     * get ServiceApi
     */
    private fun <T> getService(url: String, service: Class<T>): T = create(url).create(service)

    /**
     * save cookie to SharePreferences
     */
    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    private fun saveCookie(url: String?, domain: String?, cookies: String) {
        url ?: return
        var spUrl: String by Preference(url, cookies)
        @Suppress("UNUSED_VALUE")
        spUrl = cookies
        domain ?: return
        var spDomain: String by Preference(domain, cookies)
        @Suppress("UNUSED_VALUE")
        spDomain = cookies
    }
}

/**
 * create retrofit build
 */
class RetrofitBuild(url: String, client: OkHttpClient,
                    gsonFactory: GsonConverterFactory,
                    coroutineCallAdapterFactory: CoroutineCallAdapterFactory) {
    val retrofit: Retrofit = Retrofit.Builder().apply {
        baseUrl(url)
        client(client)
        addConverterFactory(gsonFactory)
        addCallAdapterFactory(coroutineCallAdapterFactory)
    }.build()
}