package com.payot_poin.poin.DI.Component

import com.payot_poin.poin.DI.Module.Presenter.AppPreferencePresenter
import com.payot_poin.poin.DI.PerFragment
import com.payot_poin.poin.Page.Preference.AppPreferenceFragment
import dagger.Component

/**
 * Created by yongheekim on 2018. 3. 12..
 */

@PerFragment
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(AppPreferencePresenter::class))
interface PreferenceComponent {
    fun inject(fragment: AppPreferenceFragment)
}