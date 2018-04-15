package com.payot_poin.poin.Page.Scan

import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import kr.or.payot.poin.RESTFul.MachineResponse

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface ScanContract {
    interface View {

        fun findMachineProgress()
        fun endFindMachineProgress()

        fun hasNeedCard()
        fun hasBluetoothEnable()
        fun findMachine(machine: MachineResponse)
        fun readyScan()
        fun notFondMachine()
    }

    interface Presenter {

        fun attachView(view: ScanContract.View)

        fun qrcodeView(qrCodeReaderView: QRCodeReaderView)

        fun checkFindoQrcode(): Boolean
    }
}