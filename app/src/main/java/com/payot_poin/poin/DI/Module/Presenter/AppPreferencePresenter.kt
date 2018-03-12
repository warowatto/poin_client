package com.payot_poin.poin.DI.Module.Presenter

import android.content.SharedPreferences
import com.payot_poin.poin.DI.PerFragment
import com.payot_poin.poin.Page.Preference.AppPreferenceContract
import dagger.Module
import dagger.Provides

/**
 * Created by yongheekim on 2018. 3. 12..
 */

@Module
class AppPreferencePresenter {

    @PerFragment
    @Provides
    fun providerPresenter(sharedPreferences: SharedPreferences): AppPreferenceContract.Presenter = object : AppPreferenceContract.Presenter {
        override fun autoBluetoothEnable(boolean: Boolean) {
            sharedPreferences.edit().putBoolean("auto_bluetooth", boolean)
        }
    }
}