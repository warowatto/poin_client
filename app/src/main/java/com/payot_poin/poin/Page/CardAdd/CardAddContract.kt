package com.payot_poin.poin.Page.CardAdd

import kr.or.payot.poin.RESTFul.Data.Card

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface CardAddContract {

    interface View {
        fun cardAddProgress()

        fun endCardAddProgress()

        fun sucess(card: Card)

        fun cardErrorError(message: String)
    }

    interface Presenter {
        fun attachView(view: CardAddContract.View)

        fun addCard(name: String, number: String, expiry: String, pass2digit: String, birth: String)
    }
}