package com.payot_poin.poin.DI.Component

import dagger.Component
import kr.or.payot.poin.DI.Modules.NetworkModule
import kr.or.payot.poin.RESTFul.MachineAPI
import kr.or.payot.poin.RESTFul.UserAPI
import javax.inject.Singleton

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface TestComponent {

    fun userApi(): UserAPI
    fun machineApi(): MachineAPI
}