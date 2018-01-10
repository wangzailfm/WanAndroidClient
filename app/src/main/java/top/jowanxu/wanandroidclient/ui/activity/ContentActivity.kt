package top.jowanxu.wanandroidclient.ui.activity

import Constant
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import getAgentWeb
import kotlinx.android.synthetic.main.activity_content.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.base.BaseActivity
import top.jowanxu.wanandroidclient.base.Preference
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import top.jowanxu.wanandroidclient.presenter.ContentPresenterImpl
import top.jowanxu.wanandroidclient.view.CollectArticleView

/**
 * WebViewActivity
 */
class ContentActivity : BaseActivity(), CollectArticleView {
    private lateinit var agentWeb: AgentWeb
    private lateinit var shareTitle: String
    private lateinit var shareUrl: String
    private var shareId: Int = 0
    /**
     * presenter
     */
    private val collectArticlePresenter: ContentPresenterImpl by lazy {
        ContentPresenterImpl(this)
    }
    /**
     * check login for SharedPreferences
     */
    private val isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    override fun setLayoutId(): Int = R.layout.activity_content

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolbar).init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.run {
            title = getString(R.string.loading)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        intent.extras?.let {
            shareId = it.getInt(Constant.CONTENT_ID_KEY, 0)
            shareUrl = it.getString(Constant.CONTENT_URL_KEY)
            shareTitle = it.getString(Constant.CONTENT_TITLE_KEY)
            agentWeb = shareUrl.getAgentWeb(this, webContent, LinearLayout.LayoutParams(-1, -1), receivedTitleCallback)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menuShare -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.share_article_url, getString(R.string.app_name), shareTitle, shareUrl))
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.share_title)))
                }
                return true
            }
            R.id.menuLike -> {
                // login
                if (!isLogin) {
                    Intent(this, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                    toast(getString(R.string.login_please_login))
                    return true
                }
                // Collection outside article
                if (shareId == 0) {
                    collectArticlePresenter.collectOutSideArticle(shareTitle, getString(R.string.outside_title), shareUrl, true)
                    return true
                }
                // Collection station article
                collectArticlePresenter.collectArticle(shareId, true)
                return true
            }
            R.id.menuBrowser -> {
                Intent().run {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse(shareUrl)
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (agentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }
    }



    /**
     * add article success
     * @param result HomeListResponse
     * @param isAdd true add, false remove
     */
    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        toast(if (isAdd) getString(R.string.bookmark_success) else getString(R.string.bookmark_cancel_success))
    }

    /**
     * add article false
     * @param errorMessage error message
     * @param isAdd true add, false remove
     */
    override fun collectArticleFailed(errorMessage: String?, isAdd: Boolean) {
        toast(if (isAdd) getString(R.string.bookmark_failed, errorMessage) else getString(R.string.bookmark_cancel_failed, errorMessage))
    }

    private val receivedTitleCallback = ChromeClientCallbackManager.ReceivedTitleCallback {
        _, title ->
        title?.let {
            toolbar.title = it
        }
    }
}
