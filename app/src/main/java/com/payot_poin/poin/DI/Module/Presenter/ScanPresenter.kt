package com.payot_poin.poin.DI.Module.Presenter

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.graphics.PointF
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.PerFragment
import com.payot_poin.poin.Interface.DeviceScanner
import com.payot_poin.poin.Page.Payment.PaymentActivity
import com.payot_poin.poin.Page.Scan.ScanContract
import com.payot_poin.poin.Page.Scan.ScanFragment
import dagger.Module
import dagger.Provides
import kr.or.payot.poin.RESTFul.MachineAPI

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Module
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
            if (text != null && text.length == 12) {
                view.findMachineProgress()
                deviceScanner.scan()
                        .singleOrError()
                        .flatMap { machine -> machineAPI.getMachine(machine.address) }
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