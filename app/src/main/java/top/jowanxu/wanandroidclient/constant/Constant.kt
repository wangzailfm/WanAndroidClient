import android.widget.Toast

/**
 * Constant
 */
object Constant {
    /**
     * baseUrl
     */
    const val REQUEST_BASE_URL = "http://wanandroid.com/"

    const val SHARED_NAME = "_preferences"

    const val RESULT_NULL = "result null!"

    @JvmField var showToast: Toast? = null

    const val INTERCEPTOR_ENABLE = false

    const val CONTENT_URL_KEY = "url"
    const val CONTENT_TITLE_KEY = "title"
    const val CONTENT_CID_KEY = "cid"
    const val CONTENT_CHILDREN_DATA_KEY = "childrenData"

    const val CONTENT_SHARE_TYPE = "text/plain"
}