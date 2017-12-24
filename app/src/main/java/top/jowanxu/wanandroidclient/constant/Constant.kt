import android.widget.Toast

/**
 * Constant
 */
object Constant {
    /**
     * baseUrl
     */
    const val REQUEST_BASE_URL = "http://wanandroid.com/"
    /**
     * Share preferences name
     */
    const val SHARED_NAME = "_preferences"
    const val LOGIN_KEY = "login"
    /**
     * result null
     */
    const val RESULT_NULL = "result null!"
    /**
     * Toast
     */
    @JvmField var showToast: Toast? = null
    /**
     * Debug
     */
    const val INTERCEPTOR_ENABLE = false
    /**
     * url key
     */
    const val CONTENT_URL_KEY = "url"
    /**
     * title key
     */
    const val CONTENT_TITLE_KEY = "title"
    /**
     * cid key
     */
    const val CONTENT_CID_KEY = "cid"
    /**
     * childrenData key
     */
    const val CONTENT_CHILDREN_DATA_KEY = "childrenData"
    /**
     * target key
     */
    const val CONTENT_TARGET_KEY = "target"
    /**
     * share key
     */
    const val CONTENT_SHARE_TYPE = "text/plain"
}