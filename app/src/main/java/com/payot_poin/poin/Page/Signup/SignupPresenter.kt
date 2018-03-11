package com.payot_poin.poin.Page.Signup

import android.content.Intent
import com.kakao.usermgmt.response.model.UserProfile
import com.payot_poin.poin.App
import com.payot_poin.poin.Page.Main.MainActivity
import io.reactivex.Single
import kr.or.payot.poin.RESTFul.UserAPI

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class SignupPresenter(val activity: SignupActivity, val userprofile: Single<UserProfile>, val userAPI: UserAPI) : SignupContract.Presenter {

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
                .subscribe(
                        {
                            App.user = it
                            val intent = Intent(activity, MainActivity::class.java)
                            activity.startActivity(intent)
                            activity.finish()

                        },
                        {
                            it.printStackTrace()
                        }
                )
    }


}