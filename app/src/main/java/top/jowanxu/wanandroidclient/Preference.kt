package top.jowanxu.wanandroidclient

import Constant
import android.annotation.SuppressLint
import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * SharedPreferences操作
 */
class Preference<T>(private val context: Context, private val name: String, private val default: T) : ReadWriteProperty<Any?, T> {
    private val prefs by lazy { context.getSharedPreferences(context.packageName + Constant.SHARED_NAME, Context.MODE_PRIVATE) }
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = findPreference(name, default)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <U> findPreference(name: String, default: U): U = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        res as U
    }

    @SuppressLint("CommitPrefEdits")
    private fun <U> putPreference(name: String, value: U) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }.apply()
    }
}