package com.payot_poin.poin.Page.CardAdd

import android.os.Bundle
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerCardAddComponent
import com.payot_poin.poin.DI.Module.Presenter.CardAddPresenter
import com.payot_poin.poin.Page.RootActivity
import com.payot_poin.poin.R
import com.payot_poin.poin.Utils.toast
import kr.or.payot.poin.RESTFul.Data.Card
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 10..
 */
class CardAddActivity : RootActivity(), CardAddContract.View {

    @Inject
    lateinit var presenter: CardAddContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_card_add)

        DaggerCardAddComponent.builder()
                .applicationComponent(App.component)
                .cardAddPresenter(CardAddPresenter(this))
                .build().inject(this)

        presenter.attachView(this)
    }

    override fun cardAddProgress() {
        "카드를 등록 중 입니다" toast this
    }

    override fun endCardAddProgress() {
        "카드 등록을 마쳤습니다" toast this
    }

    override fun sucess(card: Card) {
        "카드등록을 성공적으로 완료했습니다 : " + card.toString() toast this
    }

    override fun cardErrorError(message: String) {
        message toast this
    }
}