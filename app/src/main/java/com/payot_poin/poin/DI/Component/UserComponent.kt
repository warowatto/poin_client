package com.payot_poin.poin.DI.Component

import com.payot_poin.poin.DI.PerFragment
import com.payot_poin.poin.Page.User.HaveCardFragment
import com.payot_poin.poin.Page.User.PaymentListFragment
import dagger.Component

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@PerFragment
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface UserComponent {

    fun inject(fragment: HaveCardFragment)
    fun inject(fragment: PaymentListFragment)
}