package com.payot_poin.poin.DI.Component

import android.content.SharedPreferences
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Module.ApplicationModule
import com.payot_poin.poin.DI.Module.PoinDeviceModule
import com.payot_poin.poin.Interface.DeviceController
import com.payot_poin.poin.Interface.DeviceScanner
import dagger.Component
import kr.or.payot.poin.DI.Modules.KakaoModule
import kr.or.payot.poin.DI.Modules.NetworkModule
import kr.or.payot.poin.RESTFul.MachineAPI
import kr.or.payot.poin.RESTFul.UserAPI
import javax.inject.Singleton

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class, PoinDeviceModule::class, KakaoModule::class))
interface ApplicationComponent {

    fun inject(app: App)

    fun scanner(): DeviceScanner

    fun controller(): DeviceController

    fun userAAPI(): UserAPI

    fun machineAPI(): MachineAPI

    fun sharedPreference(): SharedPreferences
}