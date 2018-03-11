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

    lateinit var viewGroup: Array<View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_scan, container, false)

        qrCodeReaderView = layout.findViewById(R.id.qrcodeReaderView)
        viewGroup = arrayOf(
                layout.findViewById(R.id.viewgroup_frame),
                layout.findViewById(R.id.viewgroup_bluetooth),
                layout.findViewById(R.id.viewgroup_card))

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerScanComponent.builder()
                .applicationComponent(App.component)
                .scanPresenter(ScanPresenter(this))
                .build().inject(this)

        presenter.attachView(this)
        presenter.qrcodeView(qrCodeReaderView)

        qrCodeReaderView.startCamera()
    }

    override fun onResume() {
        super.onResume()
        presenter.checkFindoQrcode()
    }

    override fun onPause() {
        super.onPause()
        qrCodeReaderView.setQRDecodingEnabled(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        qrCodeReaderView.stopCamera()
    }


    fun allDisableViewGroup(visibleViewPosition: Int) {
        viewGroup.forEach { it.visibility = View.INVISIBLE }

        viewGroup[visibleViewPosition].visibility = View.VISIBLE
    }

    override fun findMachineProgress() {
        Toast.makeText(this.context, "장치를 찾는 중 입니다", Toast.LENGTH_SHORT).show()
    }

    override fun endFindMachineProgress() {
        Toast.makeText(this.context, "장치를 찾았습니다", Toast.LENGTH_SHORT).show()
    }

    override fun readyScan() {
        allDisableViewGroup(0)
    }

    override fun hasNeedCard() {
        allDisableViewGroup(2)
    }

    override fun hasBluetoothEnable() {
        allDisableViewGroup(1)
    }

    override fun findMachine(machine: Machine) {
        Log.d("machine", machine.toString())
        Toast.makeText(this.context, "장치를 찾았습니다", Toast.LENGTH_SHORT).show()
    }
}