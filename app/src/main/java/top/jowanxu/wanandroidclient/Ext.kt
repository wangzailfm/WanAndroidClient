
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


fun loge(tag: String, content: String?) {
    Log.e(tag, content ?: tag)
}

@SuppressLint("ShowToast")
fun Context.toast(content: String) {
    Constant.showToast?.apply {
        setText(content)
    } ?: run {
        Toast.makeText(this, content, Toast.LENGTH_SHORT)
    }.show()
}

fun Context.getStringFormat(@StringRes id: Int, vararg args: Any): String = String.format(resources.getString(id), args)

/**
 * 异步转同步
 */
suspend fun <T> asyncRequestSuspend(block: (Continuation<T>) -> Unit) = suspendCoroutine<T> {
    block(it)
}

/**
 * 协程取消
 */
suspend fun Deferred<Any>?.cancelAndJoinByActive() = this?.run {
    if (isActive) {
        this.cancelAndJoin()
    }
}

fun <T> Call<T>?.cancelCall() = this?.run {
    if (!isCanceled) {
        // cancel request
        cancel()
    }
}