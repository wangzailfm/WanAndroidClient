
import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.StringRes
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.cancelAndJoin
import retrofit2.Call
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine

/**
 * Log
 */
fun loge(tag: String, content: String?) {
    Log.e(tag, content ?: tag)
}

/**
 * show toast
 * @param content String
 */
@SuppressLint("ShowToast")
fun Context.toast(content: String) {
    Constant.showToast?.apply {
        setText(content)
    } ?: run {
        Toast.makeText(this, content, Toast.LENGTH_SHORT)
    }.show()
}

/**
 * show toast
 * @param id strings.xml
 */
fun Context.toast(@StringRes id: Int) {
    toast(getString(id))
}

/**
 * async change to sync
 */
suspend fun <T> asyncRequestSuspend(block: (Continuation<T>) -> Unit) = suspendCoroutine<T> {
    block(it)
}

/**
 * cancel coroutines
 */
suspend fun Deferred<Any>?.cancelAndJoinByActive() = this?.run {
    if (isActive) {
        this.cancelAndJoin()
    }
}

/**
 * cancel call request
 */
fun <T> Call<T>?.cancelCall() = this?.run {
    if (!isCanceled) {
        // cancel request
        cancel()
    }
}