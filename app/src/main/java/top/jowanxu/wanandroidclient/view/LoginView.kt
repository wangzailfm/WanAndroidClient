package top.jowanxu.wanandroidclient.view

import top.jowanxu.wanandroidclient.bean.LoginResponse

interface LoginView {

    /**
     * login success
     * @param result LoginResponse
     */
    fun loginSuccess(result: LoginResponse)

    /**
     * login failed
     * @param errorMessage error message
     */
    fun loginFailed(errorMessage: String?)
    /**
     * register success
     * @param result LoginResponse
     */
    fun registerSuccess(result: LoginResponse)

    /**
     * register failed
     * @param errorMessage error message
     */
    fun registerFailed(errorMessage: String?)

    /**
     * login or register success after operation
     * @param result LoginResponse
     */
    fun loginRegisterAfter(result: LoginResponse)
}