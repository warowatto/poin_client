package com.payot_poin.poin

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.payot_poin.poin.DI.Component.DaggerTestComponent
import io.reactivex.schedulers.Schedulers
import kr.or.payot.poin.DI.Modules.NetworkModule
import kr.or.payot.poin.RESTFul.UserAPI

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    lateinit var api:UserAPI

    @Before
    fun before() {
        api = DaggerTestComponent.builder()
                .networkModule(NetworkModule())
                .build().userApi()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        // assertEquals("com.payot_poin.poin", appContext.packageName)

        api.login("kakao", "729392884")
                .observeOn(Schedulers.trampoline())
                .subscribe(
                        {
                            println(it)
                        },
                        {
                            it.printStackTrace()
                        }
                )
    }
}
