package com.payot_poin.poin.Page.Login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.util.exception.KakaoException
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerLoginComponent
import com.payot_poin.poin.DI.Module.Presenter.LoginPresenter
import com.payot_poin.poin.Page.RootActivity
import com.payot_poin.poin.R
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 10..
 */
class LoginActivity : RootActivity(), LoginContract.View, ISessionCallback {

    @Inject
    lateinit var presenter: LoginContract.Presenter

    lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        DaggerLoginComponent.builder()
                .applicationComponent(App.component)
                .loginPresenter(LoginPresenter(this))
                .build().inject(this)

        presenter.attachView(this)

        progress = findViewById(R.id.progressbar_login)

        Session.getCurrentSession().addCallback(this)
        Session.getCurrentSession().checkAndImplicitOpen()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        Session.getCurrentSession().removeCallback(this)
        super.onDestroy()
    }

    override fun findUser() {
        progress.visibility = View.VISIBLE
    }

    override fun endFindUser() {
        progress.visibility = View.INVISIBLE
    }

    override fun onSessionOpenFailed(exception: KakaoException?) {
        this.finish()
    }

    override fun onSessionOpened() {
        presenter.checkedUser()
    }
}