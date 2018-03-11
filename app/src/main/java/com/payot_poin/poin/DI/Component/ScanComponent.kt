package com.payot_poin.poin.DI.Component

import com.payot_poin.poin.DI.Module.Presenter.ScanPresenter
import com.payot_poin.poin.DI.PerFragment
import com.payot_poin.poin.Page.Scan.ScanFragment
import dagger.Component

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@PerFragment
@Component(
        modules = arrayOf(ScanPresenter::class), dependencies = arrayOf(ApplicationComponent::class))
interface ScanComponent {
    fun inject(fragment: ScanFragment)
}