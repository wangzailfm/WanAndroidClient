package top.jowanxu.wanandroidclient.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.jowanxu.wanandroidclient.R

class TypeFragment : Fragment() {

    private var mainView: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView =inflater?.inflate(R.layout.fragment_type, null)
        }
        return mainView
    }
}