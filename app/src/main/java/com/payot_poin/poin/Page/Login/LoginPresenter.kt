package com.payot_poin.poin.Page.Login

import android.content.Intent
import com.kakao.usermgmt.response.model.UserProfile
import com.payot_poin.poin.App
import com.payot_poin.poin.Page.Main.MainActivity
import com.payot_poin.poin.Page.Signup.SignupActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.or.payot.poin.RESTFul.UserAPI
import retrofit2.HttpException

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class LoginPresenter(val loginActivity: LoginActivity, val userAPI: UserAPI, val requestMe: Single<UserProfile>) : LoginContract.Presenter {


    lateinit var view: LoginContract.View

    override fun attachView(view: LoginContract.View) {
        this.view = view
    }

    override fun checkedUser() {
        view.findUser()
        requestMe.flatMap { userAPI.login("kakao", it.id.toString()) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose { view.endFindUser() }
                .subscribe(
                        {
                            App.user = it
                            val intent = Intent(loginActivity, MainActivity::class.java)
                            loginActivity.startActivity(intent)
                            loginActivity.finish()
                        },
                        {
                            when (it) {
                                is HttpException -> {
                                    val intent = Intent(loginActivity, SignupActivity::class.java)
                                    loginActivity.startActivity(intent)
                                }
                            }
                        }
                )
    }

}