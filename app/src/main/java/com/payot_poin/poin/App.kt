package com.payot_poin.poin

import android.app.Application
import com.kakao.auth.KakaoAdapter
import com.kakao.auth.KakaoSDK
import com.payot_poin.poin.DI.Component.ApplicationComponent
import com.payot_poin.poin.DI.Component.DaggerApplicationComponent
import com.payot_poin.poin.DI.Module.ApplicationModule
import com.payot_poin.poin.DI.Module.PoinDeviceModule
import kr.or.payot.poin.DI.Modules.KakaoModule
import kr.or.payot.poin.DI.Modules.NetworkModule
import kr.or.payot.poin.RESTFul.Data.User
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class App : Application() {

    companion object {
        lateinit var component: ApplicationComponent
        var user: User? = null
    }

    @Inject
    lateinit var kakaoAdapter: KakaoAdapter

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .kakaoModule(KakaoModule())
                .networkModule(NetworkModule())
                .poinDeviceModule(PoinDeviceModule())
                .build()

        component.inject(this)

        KakaoSDK.init(kakaoAdapter)
    }
}