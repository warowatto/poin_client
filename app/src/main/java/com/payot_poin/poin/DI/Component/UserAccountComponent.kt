package com.payot_poin.poin.DI.Component

import com.payot_poin.poin.DI.Module.Presenter.UserPresenter
import com.payot_poin.poin.DI.PerFragment
import com.payot_poin.poin.Page.User.AccountInfoFragment
import com.payot_poin.poin.Page.User.HaveCardFragment
import com.payot_poin.poin.Page.User.PaymentListFragment
import dagger.Component

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@PerFragment
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(UserPresenter::class))
interface UserAccountComponent {

    fun inject(fragment: AccountInfoFragment)
}