package com.payot_poin.poin.DI.Module.Presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.bluetooth.BluetoothDevice
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.PerActivity
import com.payot_poin.poin.Interface.DeviceController
import com.payot_poin.poin.Page.Payment.PaymentActivity
import com.payot_poin.poin.Page.Payment.PaymentContract
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.operators.completable.CompletableDisposeOn
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.*
import kr.or.payot.poin.RESTFul.Data.Card
import kr.or.payot.poin.RESTFul.Data.Machine
import kr.or.payot.poin.RESTFul.Data.Product
import kr.or.payot.poin.RESTFul.MachineResponse
import kr.or.payot.poin.RESTFul.UserAPI
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Module
class PaymentPresenter(val activity: PaymentActivity) {

    @PerActivity
    @Provides
    fun providePresenter(deviceController: DeviceController, userAPI: UserAPI): PaymentContract.Presenter = object : PaymentContract.Presenter, LifecycleObserver {
        lateinit var view: PaymentContract.View
        lateinit var machine: Machine

        val dispose = CompositeDisposable()

        override fun attachview(view: PaymentContract.View) {
            this.view = view
            activity.lifecycle.addObserver(this)


            val device: MachineResponse = activity.intent.extras.getParcelable("device")
            val realDevice: BluetoothDevice = activity.intent.extras.getParcelable("realDevice")

            this.machine = device.machine

            // 상품 리스트를 출력
            view.showProduct(device.products)

            // 업체정보
            view.showCompanyInfo(device.company)

            // 장치정보
            view.showDeviceInfo(device.machine)

            view.progressToService()
            deviceController.connect(activity, realDevice)
                    .subscribe(
                            {
                                when (it) {
                                    1000 -> view.readyToService()
                                }
                            },
                            {
                                activity.finish()
                            }
                    )
                    .addTo(dispose)

        }

        override fun payment(product: Product, card: Card, point: Int) {
            view.progressPayment()

            val userId = App.user?.id!!
//            val paymentObserver = userAPI.payment(userId, card.id, machine.id, product.productId, product.price, point)
            val paymentObserver = userAPI.payment(userId, card.id, machine.id, product.productId, 100, point)

            paymentObserver
                    .flatMap {
                        insertCoinObserver(userId, product.price)
                    }
//            insertCoinObserver(userId, product.price)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnEvent { _, _ -> view.endProgressPayment() }
                    .subscribe(
                            {
                                if (it[3] == "OK") {
                                    view.paymentSuccess()
                                } else {

                                }
                            },
                            {
                                it.printStackTrace()
                                when (it) {
                                    is HttpException -> {
                                        val message = it.response().message().toString()
                                        view.paymentError(message)
                                    }
                                    is IllegalStateException -> view.errorAuthDevice()
                                }
                            }
                    )


        }

        fun insertCoinObserver(userId: Int, coin: Int): Single<List<String>> =
                deviceController.sendMessage("CMD S 1 ${getTime(Date())} ${userId}")
                        .flatMap {
                            if (it[3] == "OK") {
                                return@flatMap deviceController.sendMessage("CMD S ${coin}")
                            } else {
                                throw IllegalArgumentException("인증처리가 정상적으로 이루지지 않았습니다")
                            }
                        }.singleOrError()

        fun getTime(date: Date): String = SimpleDateFormat("yyMMddHHmmss").format(date)

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun disconnec() {
            deviceController.disconnect()
            dispose.dispose()
        }

    }
}