package com.payot_poin.poin.DI.Module.Presenter

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.graphics.PointF
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Module.PoinDeviceModule
import com.payot_poin.poin.DI.PerFragment
import com.payot_poin.poin.Interface.DeviceScanner
import com.payot_poin.poin.Page.Payment.PaymentActivity
import com.payot_poin.poin.Page.Scan.ScanContract
import com.payot_poin.poin.Page.Scan.ScanFragment
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kr.or.payot.poin.RESTFul.MachineAPI
import java.util.concurrent.TimeUnit

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Module(includes = arrayOf(PoinDeviceModule::class))
class ScanPresenter(val fragment: ScanFragment) {

    @PerFragment
    @Provides
    fun providePresenter(machineAPI: MachineAPI, deviceScanner: DeviceScanner): ScanContract.Presenter = object : ScanContract.Presenter, QRCodeReaderView.OnQRCodeReadListener {

        lateinit var view: ScanContract.View
        lateinit var qrCodeReaderView: QRCodeReaderView

        override fun attachView(view: ScanContract.View) {
            this.view = view
        }

        override fun qrcodeView(qrCodeReaderView: QRCodeReaderView) {
            this.qrCodeReaderView = qrCodeReaderView
            qrCodeReaderView.startCamera()
            qrCodeReaderView.setOnQRCodeReadListener(this)
        }

        override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
            qrCodeReaderView.setQRDecodingEnabled(false)
            println(text)
            if (text != null && text.length == 17) {
                view.findMachineProgress()
                deviceScanner.scan(text)
                        .timeout(2, TimeUnit.SECONDS, Schedulers.computation())
                        .flatMap { machine -> machineAPI.getMachine(text).map { it to machine } }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEvent { _, _ -> view.endFindMachineProgress() }
                        .subscribe(
                                {

                                    view.findMachine(it.first)
                                    val context = fragment.context
                                    val intent = Intent(context, PaymentActivity::class.java)
                                    intent.putExtra("device", it.first)
                                    intent.putExtra("realDevice", it.second)

                                    context?.startActivity(intent)
                                },
                                {
                                    view.notFondMachine()
                                    qrCodeReaderView.setQRDecodingEnabled(true)
                                })
            } else {
                checkFindoQrcode()
            }
        }

        override fun checkFindoQrcode(): Boolean {
            val user = App.user

            qrCodeReaderView.setQRDecodingEnabled(false)
            if (user != null && user.cards?.isEmpty() ?: true) {
                // 카드등록이 필요할때
                view.hasNeedCard()
                return false
            } else if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
                view.hasBluetoothEnable()
                return false
            } else {
                qrCodeReaderView.setQRDecodingEnabled(true)
                view.readyScan()
                return true
            }
        }

    }
}