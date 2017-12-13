
import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine


fun loge(tag: String, content: String?) {
    Log.e(tag, content ?: tag)
}

fun Context.toast(content: String) {
    Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
}

/**
 * 异步转同步
 */
suspend fun <T> asyncRequestSuspend(block: (Continuation<T>) -> Unit) = suspendCoroutine<T> {
    block(it)
}
