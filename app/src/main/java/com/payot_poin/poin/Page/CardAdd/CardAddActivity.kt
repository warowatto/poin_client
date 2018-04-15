package com.payot_poin.poin.Page.CardAdd

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerCardAddComponent
import com.payot_poin.poin.DI.Module.Presenter.CardAddPresenter
import com.payot_poin.poin.Page.RootActivity
import com.payot_poin.poin.R
import com.payot_poin.poin.Utils.toast
import kotlinx.android.synthetic.main.fragment_card_add.*
import kr.or.payot.poin.RESTFul.Data.Card
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 10..
 */
class CardAddActivity : RootActivity(), CardAddContract.View {

    @Inject
    lateinit var presenter: CardAddContract.Presenter

    val progress: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            setCancelable(false)
            setTitle("카드등록")
            setMessage("카드를 등록중 입니다")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_card_add)

        DaggerCardAddComponent.builder()
                .applicationComponent(App.component)
                .cardAddPresenter(CardAddPresenter(this))
                .build().inject(this)

        presenter.attachView(this)

        initView()
    }

    private fun initView() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_cardadd)
        setSupportActionBar(toolbar)

        val arrow = resources.getDrawable(R.drawable.abc_ic_ab_back_material)
        arrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(arrow)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.btnCardAdd).setOnClickListener {
            val cardName = editCardName.text.toString()
            val cardNumber = editCardNumber1.text.toString() + "-" + editCardNumber2.text.toString() + "-" + editCardNumber3.text.toString() + "-" + editCardNumber4.text.toString()
            val cardExpiry = editCardExpiry1.text.toString() + "-" + editCardExpiry2.text.toString()
            val cardPass = editCardPass.text.toString()
            val birth = editCardBirth.text.toString()
            presenter.addCard(cardName, cardNumber, cardExpiry, cardPass, birth)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> this.finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun cardAddProgress() {
        progress.show()
    }

    override fun endCardAddProgress() {
        progress.dismiss()
    }

    override fun sucess(card: Card) {
        val message = "${card.displayName}이 (${card.bankName}) 정상적으로 등록되었습니다"

        AlertDialog.Builder(this)
                .setTitle("카드 등록 완료")
                .setMessage(message)
                .setNeutralButton("확인") { dialog, _ -> dialog.dismiss() }
                .setOnDismissListener { this.finish() }
                .show()
    }

    override fun cardErrorError(message: String) {
        message toast this
    }
}