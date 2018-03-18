package com.payot_poin.poin.Page.Signup

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerSignupComponent
import com.payot_poin.poin.DI.Module.Presenter.SignupPresenter
import com.payot_poin.poin.Page.RootActivity
import com.payot_poin.poin.R
import kotlinx.android.synthetic.main.activity_signup.*
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class SignupActivity : RootActivity(), SignupContract.View {

    @Inject
    lateinit var presenter: SignupContract.Presenter

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            setTitle("회원가입")
            setMessage("회원을 등록중입니다")
            setCancelable(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        DaggerSignupComponent.builder()
                .applicationComponent(App.component)
                .signupPresenter(SignupPresenter(this))
                .build().inject(this)

        presenter.attachView(this)

        initView()
    }

    private fun initView() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_signup)
        setSupportActionBar(toolbar)

        val arrow = resources.getDrawable(R.drawable.abc_ic_ab_back_material)
        arrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(arrow)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSignup.setOnClickListener {
            val email = editEmail.text.toString()
            val gender = if (radio_man.isChecked) "M" else "F"
            val name = editName.text.toString()

            presenter.signup(email, name, gender)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> this.finish()
        }

        return true
    }

    override fun signupProgress() {
        progressDialog.show()
    }

    override fun endSignupProgress() {
        progressDialog.dismiss()
    }

    override fun errorSignup(message: String) {
        AlertDialog.Builder(this)
                .setTitle("오류")
                .setMessage(message)
                .setNeutralButton("확인") { dialog, _ -> dialog.dismiss()}
                .show()
    }
}