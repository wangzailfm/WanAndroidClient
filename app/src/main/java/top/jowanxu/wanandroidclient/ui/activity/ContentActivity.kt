package top.jowanxu.wanandroidclient.ui.activity

import Constant
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.app_bar_main.*
import top.jowanxu.wanandroidclient.R

class ContentActivity : AppCompatActivity() {
    private lateinit var agentWeb: AgentWeb
    private lateinit var shareTitle: String
    private lateinit var shareUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        toolbarBack.run {
            visibility = View.VISIBLE
            setOnClickListener(onClickListener)
        }
        toolbarShare.run {
            visibility = View.VISIBLE
            setOnClickListener(onClickListener)
        }
        intent.extras?.let {
            shareUrl = it.getString(Constant.CONTENT_URL_KEY)
            shareTitle = it.getString(Constant.CONTENT_TITLE_KEY)
            agentWeb = AgentWeb.with(this)//传入Activity or Fragment
                    .setAgentWebParent(webContent, LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件
                    .useDefaultIndicator()// 使用默认进度条
                    .defaultProgressBarColor() // 使用默认进度条颜色
                    .setReceivedTitleCallback(receivedTitleCallback) //设置 Web 页面的 title 回调
                    .createAgentWeb()//
                    .ready()
                    .go(shareUrl)
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
                finish()
                super.onKeyDown(keyCode, event)
            }

    private val receivedTitleCallback = ChromeClientCallbackManager.ReceivedTitleCallback {
        _, title ->
        title?.let {
            toolbarTitle.text = it
        }
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.toolbarBack -> {
                finish()
            }
            R.id.toolbarShare -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${getString(R.string.app_name)}分享：$shareUrl")
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, "分享"))
                }
            }
        }
    }
}
