package top.jowanxu.wanandroidclient.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import toast
import top.jowanxu.wanandroidclient.R
import top.jowanxu.wanandroidclient.base.BaseActivity
import top.jowanxu.wanandroidclient.ui.fragment.HomeFragment
import top.jowanxu.wanandroidclient.ui.fragment.MineFragment
import top.jowanxu.wanandroidclient.ui.fragment.TypeFragment

/**
 * 主界面
 */
class MainActivity : BaseActivity() {
    private var lastTime: Long = 0

    /**
     * HomeFragment
     */
    private var homeFragment: HomeFragment? = null
    /**
     * TypeFragment
     */
    private var typeFragment: TypeFragment? = null
    /**
     * MineFragment
     */
    private var mineFragment: MineFragment? = null
    /**
     * FragmentManager
     */
    private val fragmentManager by lazy {
        supportFragmentManager
    }
    /**
     * current Index
     */
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }
        navigation.run {
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_home
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // open search
        if (item.itemId == R.id.menuSearch) {
            Intent(this, SearchActivity::class.java).run {
                startActivity(this)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 防止重叠
     */
    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is HomeFragment -> homeFragment ?: let { homeFragment = fragment }
            is TypeFragment -> typeFragment ?: let { typeFragment = fragment }
            is MineFragment -> mineFragment ?: let { mineFragment = fragment }
        }
    }

    /**
     * 退出
     */
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime < 2 * 1000) {
            super.onBackPressed()
            finish()
        } else {
            toast("再按一次退出")
            lastTime = currentTime
        }
    }

    /**
     * 显示对应Fragment
     */
    private fun setFragment(index: Int) {
        fragmentManager.beginTransaction().apply {
            homeFragment ?: let {
                HomeFragment().let {
                    homeFragment = it
                    add(R.id.content, it)
                }
            }
            typeFragment ?: let {
                TypeFragment().let {
                    typeFragment = it
                    add(R.id.content, it)
                }
            }
            mineFragment ?: let {
                MineFragment().let {
                    mineFragment = it
                    add(R.id.content, it)
                }
            }
            hideFragment(this)
            when (index) {
                R.id.navigation_home -> {
                    homeFragment?.let {
                        this.show(it)
                    }
                }
                R.id.navigation_type -> {
                    typeFragment?.let {
                        this.show(it)
                    }
                }
                R.id.navigation_mine -> {
                    mineFragment?.let {
                        this.show(it)
                    }
                }
            }
        }.commit()
    }

    /**
     * 隐藏所有fragment
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        homeFragment?.let {
            transaction.hide(it)
        }
        typeFragment?.let {
            transaction.hide(it)
        }
        mineFragment?.let {
            transaction.hide(it)
        }
    }

    /**
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                setFragment(item.itemId)
                return@OnNavigationItemSelectedListener when (item.itemId) {
                    R.id.navigation_home -> {
                        if (currentIndex == R.id.navigation_home) {
                            homeFragment?.smoothScrollToPosition()
                        }
                        currentIndex = R.id.navigation_home
                        true
                    }
                    R.id.navigation_type -> {
                        currentIndex = R.id.navigation_type
                        true
                    }
                    R.id.navigation_mine -> {
                        currentIndex = R.id.navigation_mine
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
}
