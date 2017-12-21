package top.jowanxu.wanandroidclient.base

import android.app.Application
import com.squareup.leakcanary.LeakCanary

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
    }
}