package com.payot_poin.poin.Page.Signup

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface SignupContract {

    interface View {
        fun signupProgress()

        fun endSignupProgress()
    }

    interface Presenter {

        fun attachView(view: SignupContract.View)

        fun signup(email: String, name:String, gender: String)
    }
}