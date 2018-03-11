package com.payot_poin.poin.DI.Component

import com.payot_poin.poin.DI.Module.Presenter.CardAddPresenter
import com.payot_poin.poin.DI.PerActivity
import com.payot_poin.poin.Page.CardAdd.CardAddActivity
import dagger.Component

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(CardAddPresenter::class))
interface CardAddComponent {
    fun inject(activity: CardAddActivity)
}