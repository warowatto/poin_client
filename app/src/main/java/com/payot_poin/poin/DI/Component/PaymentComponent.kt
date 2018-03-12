package com.payot_poin.poin.DI.Component

import com.payot_poin.poin.DI.Module.Presenter.PaymentPresenter
import com.payot_poin.poin.DI.PerActivity
import com.payot_poin.poin.Page.Payment.PaymentActivity
import dagger.Component

/**
 * Created by yongheekim on 2018. 3. 12..
 */

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(PaymentPresenter::class))
interface PaymentComponent {

    fun inject(activity: PaymentActivity)
}