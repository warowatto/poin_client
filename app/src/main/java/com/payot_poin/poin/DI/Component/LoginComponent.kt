package com.payot_poin.poin.DI.Component

import com.payot_poin.poin.DI.Module.Presenter.LoginPresenter
import com.payot_poin.poin.DI.PerActivity
import com.payot_poin.poin.Page.Login.LoginActivity
import dagger.Component

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(LoginPresenter::class))
interface LoginComponent {

    fun inject(activity: LoginActivity)
}