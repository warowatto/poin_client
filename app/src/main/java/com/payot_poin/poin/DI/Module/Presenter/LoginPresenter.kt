package com.payot_poin.poin.DI.Module.Presenter

import android.content.Intent
import com.kakao.usermgmt.response.model.UserProfile
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.PerActivity
import com.payot_poin.poin.Page.Login.LoginActivity
import com.payot_poin.poin.Page.Login.LoginContract
import com.payot_poin.poin.Page.Main.MainActivity
import com.payot_poin.poin.Page.Signup.SignupActivity
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.or.payot.poin.RESTFul.UserAPI

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Module
class LoginPresenter(val activity: LoginActivity) {

    @PerActivity
    @Provides
    fun providePresenter(userAPI: UserAPI, userProfile: Single<UserProfile>): LoginContract.Presenter = object : LoginContract.Presenter {
        lateinit var view: LoginContract.View

        override fun attachView(view: LoginContract.View) {
            this.view = view
        }

        override fun checkedUser() {

            view.findUser()
            userProfile.flatMap { userAPI.login("kakao", it.id.toString()) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnEvent { _, _ -> view.endFindUser() }
                    .subscribe(
                            {
                                App.user = it
                                val intent = Intent(activity, MainActivity::class.java)
                                activity.startActivity(intent)
                                activity.finish()
                            },
                            {
                                val intent = Intent(activity, SignupActivity::class.java)
                                activity.startActivity(intent)
                                activity.finish()
                            }
                    )
        }
    }
}