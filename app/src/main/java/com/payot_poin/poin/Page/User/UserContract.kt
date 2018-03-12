package com.payot_poin.poin.Page.User

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface UserContract {

    interface View {

    }

    interface Presenter {

        fun unregistUser(userId: Int)

        fun logout()
    }
}