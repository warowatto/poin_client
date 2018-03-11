package com.payot_poin.poin.Page.Scan

import android.arch.lifecycle.LifecycleObserver
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import kr.or.payot.poin.RESTFul.Data.Machine

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface ScanContract {
    interface View {

        fun findMachineProgress()
        fun endFindMachineProgress()

        fun hasNeedCard()

        fun hasBluetoothEnable()

        fun findMachine(machine: Machine)
    }

    interface Presenter {

        fun attachView(view: ScanContract.View)

        fun qrcodeView(qrCodeReaderView: QRCodeReaderView)

        fun scanning()

        fun stopScanning()
    }
}