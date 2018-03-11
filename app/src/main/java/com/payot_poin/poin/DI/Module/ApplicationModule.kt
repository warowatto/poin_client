package com.payot_poin.poin.DI.Module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Module
class ApplicationModule(val app: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideSharedPreference(): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(app.applicationContext)
}