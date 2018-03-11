package com.payot_poin.poin.DI.Module.Presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.PerFragment
import com.payot_poin.poin.Page.Payment.PaymentActivity
import com.payot_poin.poin.Page.Scan.ScanContract
import com.payot_poin.poin.Page.Scan.ScanFragment
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.or.payot.poin.RESTFul.MachineAPI

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Module
class ScanPresenter(val fragment: ScanFragment) {

    @PerFragment
    @Provides
    fun providePresenter(machineAPI: MachineAPI): ScanContract.Presenter = object : ScanContract.Presenter, LifecycleObserver {

        lateinit var view: ScanContract.View
        lateinit var qrCodeReaderView: QRCodeReaderView

        override fun attachView(view: ScanContract.View) {
            this.view = view
            fragment.lifecycle.addObserver(this)
        }

        override fun qrcodeView(qrCodeReaderView: QRCodeReaderView) {
            this.qrCodeReaderView = qrCodeReaderView

            this.qrCodeReaderView = qrCodeReaderView.apply {
                setBackCamera()
            }
        }

        fun qrcodeScanObserver(): Single<String> =
                Single.create { emitter ->
                    val callback = QRCodeReaderView.OnQRCodeReadListener { text, _ ->
                        emitter.onSuccess(text)
                        qrCodeReaderView.setQRDecodingEnabled(false)
                    }

                    qrCodeReaderView.setOnQRCodeReadListener(callback)
                    qrCodeReaderView.setQRDecodingEnabled(true)
                }

        override fun scanning() {
            view.findMachineProgress()

            qrcodeScanObserver()
                    .flatMap { machineAPI.getMachine(it) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnEvent { _, _ -> view.endFindMachineProgress() }
                    .subscribe(
                            {
                                val context = fragment.context
                                val intent = Intent(context, PaymentActivity::class.java)
                                intent.putExtra("device", it)

                                context?.startActivity(intent)
                            },
                            {
                                it.printStackTrace()
                            }
                    )
        }

        override fun stopScanning() {
            qrCodeReaderView.setQRDecodingEnabled(false)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun startCamera() {
            qrCodeReaderView.startCamera()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun checkFindoQrcode(): Boolean {
            val user = App.user

            if (user != null && user.cards?.isEmpty() ?: true) {
                // 카드등록이 필요할때
                view.hasNeedCard()
                return false
            } else if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
                view.hasBluetoothEnable()
                return false
            } else {
                return true
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun scanStoped() {
            stopScanning()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun stopCamera() {
            qrCodeReaderView.stopCamera()
        }

    }
}