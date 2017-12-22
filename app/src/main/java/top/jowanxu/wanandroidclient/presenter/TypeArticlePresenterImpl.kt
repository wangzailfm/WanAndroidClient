package top.jowanxu.wanandroidclient.presenter

import top.jowanxu.wanandroidclient.bean.ArticleListResponse
import top.jowanxu.wanandroidclient.model.TypeArticleModel
import top.jowanxu.wanandroidclient.model.TypeArticleModelImpl
import top.jowanxu.wanandroidclient.view.TypeArticleFragmentView

class TypeArticlePresenterImpl(private val typeArticleFragmentView: TypeArticleFragmentView) : TypeArticlePresenter, TypeArticlePresenter.OnTypeArticleListListener {

    private val typeArticleModel: TypeArticleModel = TypeArticleModelImpl()

    /**
     * get Type Article list
     * @param page page
     * @param cid cid
     */
    override fun getTypeArticleList(page: Int, cid: Int) {
        typeArticleModel.getTypeArticleList(this, page, cid)
    }

    /**
     * get Type Article list success
     * @param result ArticleListResponse
     */
    override fun getTypeArticleListSuccess(result: ArticleListResponse) {
        typeArticleFragmentView.getTypeArticleListAfter()
        val total = result.data.total
        if (total == 0) {
            typeArticleFragmentView.getTypeArticleListZero()
            return
        }
        if (total < result.data.size) {
            typeArticleFragmentView.getTypeArticleListSmall(result)
            return
        }
        typeArticleFragmentView.getTypeArticleListSuccess(result)
    }

    /**
     * get Type Article list failed
     * @param errorMessage error message
     */
    override fun getTypeArticleListFailed(errorMessage: String?) {
        typeArticleFragmentView.getTypeArticleListAfter()
        typeArticleFragmentView.getTypeArticleListFailed(errorMessage)
    }

    fun cancelRequest() {
        typeArticleModel.cancelRequest()
    }
}