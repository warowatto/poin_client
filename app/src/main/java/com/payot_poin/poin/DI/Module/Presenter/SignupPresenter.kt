package com.payot_poin.poin.DI.Module.Presenter

import android.content.Intent
import com.kakao.usermgmt.response.model.UserProfile
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.PerActivity
import com.payot_poin.poin.Page.Main.MainActivity
import com.payot_poin.poin.Page.Signup.SignupActivity
import com.payot_poin.poin.Page.Signup.SignupContract
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import kr.or.payot.poin.RESTFul.UserAPI

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Module
class SignupPresenter(val activity: SignupActivity) {

    @PerActivity
    @Provides
    fun providePresenter(userprofile: Single<UserProfile>, userAPI: UserAPI): SignupContract.Presenter = object : SignupContract.Presenter {
        lateinit var view: SignupContract.View

        override fun attachView(view: SignupContract.View) {
            this.view = view
        }

        override fun signup(email: String, name: String, gender: String) {
            view.signupProgress()

            userprofile
                    .flatMap {
                        userAPI.signup("kakao", it.id.toString(), email, name, gender, it.profileImagePath, it.thumbnailImagePath)
                    }
                    .doOnEvent { _, _ -> view.endSignupProgress() }
                    .subscribe(
                            {
                                App.user = it
                                val intent = Intent(activity, MainActivity::class.java)
                                activity.startActivity(intent)
                                activity.finish()

                            },
                            {
                                view.errorSignup("이미 가입되어 있는 계정입니다")
                            }
                    )
        }
    }
}