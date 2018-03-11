package com.payot_poin.poin.Page.Scan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerScanComponent
import com.payot_poin.poin.DI.Module.Presenter.ScanPresenter
import com.payot_poin.poin.Page.RootFragment
import com.payot_poin.poin.R
import kr.or.payot.poin.RESTFul.Data.Machine
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class ScanFragment : RootFragment(), ScanContract.View {

    @Inject
    lateinit var presenter: ScanContract.Presenter

    lateinit var qrCodeReaderView: QRCodeReaderView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_scan, container, false)

        qrCodeReaderView = layout.findViewById(R.id.qrcodeReaderView)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerScanComponent.builder()
                .applicationComponent(App.component)
                .scanPresenter(ScanPresenter(this))
                .build().inject(this)

        presenter.attachView(this)
    }

    override fun findMachineProgress() {
        Toast.makeText(this.context, "장치를 찾는 중 입니다", Toast.LENGTH_SHORT).show()
    }

    override fun endFindMachineProgress() {
        Toast.makeText(this.context, "장치를 찾았습니다", Toast.LENGTH_SHORT).show()
    }

    override fun hasNeedCard() {
        Toast.makeText(this.context, "카드가 필요합니다", Toast.LENGTH_SHORT).show()
    }

    override fun hasBluetoothEnable() {
        Toast.makeText(this.context, "블루투스를 활성화 해주십시오", Toast.LENGTH_SHORT).show()
    }

    override fun findMachine(machine: Machine) {
        Log.d("machine", machine.toString())
        Toast.makeText(this.context, "장치를 찾았습니다", Toast.LENGTH_SHORT).show()
    }
}