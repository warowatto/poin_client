package com.payot_poin.poin.Page.Login

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface LoginContract {

    interface View {
        fun findUser()

        fun endFindUser()
    }

    interface Presenter {

        fun attachView(view: LoginContract.View)
        fun checkedUser()
    }
}