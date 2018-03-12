package com.payot_poin.poin.DI.Module.Presenter

import android.content.Intent
import com.kakao.auth.Session
import com.kakao.usermgmt.response.model.UserProfile
import com.payot_poin.poin.DI.PerFragment
import com.payot_poin.poin.Page.Login.LoginActivity
import com.payot_poin.poin.Page.User.AccountInfoFragment
import com.payot_poin.poin.Page.User.UserContract
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.or.payot.poin.RESTFul.UserAPI

/**
 * Created by yongheekim on 2018. 3. 12..
 */

@Module
class UserPresenter(val fragment: AccountInfoFragment) {

    @PerFragment
    @Provides
    fun providePresenter(userpofile: Single<UserProfile>, userAPI: UserAPI): UserContract.Presenter = object : UserContract.Presenter {
        override fun unregistUser(userId: Int) {
            userpofile.flatMap { userAPI.unregist("kakao", it.id.toString()) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                logout()
                            },
                            {
                                it.printStackTrace()
                            }
                    )
        }

        override fun logout() {
            Session.getCurrentSession().close()
            fragment.startActivity(Intent(fragment.context, LoginActivity::class.java))
            fragment.activity?.finish()
        }

    }
}