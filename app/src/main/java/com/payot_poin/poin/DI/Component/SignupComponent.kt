package com.payot_poin.poin.DI.Component

import com.payot_poin.poin.DI.Module.Presenter.SignupPresenter
import com.payot_poin.poin.DI.PerActivity
import com.payot_poin.poin.Page.Signup.SignupActivity
import dagger.Component

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(SignupPresenter::class))
interface SignupComponent {
    fun inject(activity: SignupActivity)
}