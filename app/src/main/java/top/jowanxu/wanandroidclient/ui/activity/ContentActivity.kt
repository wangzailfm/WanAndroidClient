package top.jowanxu.wanandroidclient.ui.activity

import Constant
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.LinearLayout
import com.just.library.AgentWeb
import com.just.library.ChromeClientCallbackManager
import getStringFormat
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.app_bar_main.*
import top.jowanxu.wanandroidclient.R

class ContentActivity : AppCompatActivity() {
    private lateinit var agentWeb: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        intent.extras?.let {
            it.getString(Constant.CONTENT_TITLE_KEY)?.let {
                toolbarTitle.text = if (it.length > 10) getStringFormat(R.string.web_title, it.substring(0, 10)) else it
            }
            agentWeb = AgentWeb.with(this)//传入Activity or Fragment
                    .setAgentWebParent(webContent, LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件
                    .useDefaultIndicator()// 使用默认进度条
                    .defaultProgressBarColor() // 使用默认进度条颜色
                    .setReceivedTitleCallback(receivedTitleCallback) //设置 Web 页面的 title 回调
                    .createAgentWeb()//
                    .ready()
                    .go(it.getString(Constant.CONTENT_URL_KEY))
        }
    }

    override fun onPause() {
        agentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        agentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        agentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean =
            if (agentWeb.handleKeyEvent(keyCode, event)) {
                true
            } else {
                super.onKeyDown(keyCode, event)
            }

    private val receivedTitleCallback = ChromeClientCallbackManager.ReceivedTitleCallback {
        _, title ->
        title?.let {
            toolbarTitle.text = if (it.length > 10) getStringFormat(R.string.web_title, it.substring(0, 10)) else it
        }
    }
}
