package com.payot_poin.poin.Page.Scan

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerScanComponent
import com.payot_poin.poin.DI.Module.Presenter.ScanPresenter
import com.payot_poin.poin.Page.CardAdd.CardAddActivity
import com.payot_poin.poin.Page.RootFragment
import com.payot_poin.poin.R
import kr.or.payot.poin.RESTFul.MachineResponse
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class ScanFragment : RootFragment(), ScanContract.View {


    @Inject
    lateinit var presenter: ScanContract.Presenter

    lateinit var qrCodeReaderView: QRCodeReaderView

    lateinit var viewGroup: Array<View>

    val progress:ProgressDialog by lazy {
        ProgressDialog(activity).apply {
            setCancelable(false)
            setMessage("장치를 검색 중 입니다")
        }
    }

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

        val bluetoothButton = viewGroup[1].findViewById<Button>(R.id.btnBluetooth)
        val cardAddButton = viewGroup[2].findViewById<Button>(R.id.btnCardAdd)

        cardAddButton.setOnClickListener {
            val intent = Intent(activity, CardAddActivity::class.java)
            startActivity(intent)
        }

        bluetoothButton.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS))
        }
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
        progress.show()
    }

    override fun endFindMachineProgress() {
        progress.dismiss()
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

    override fun findMachine(machine: MachineResponse) {

    }
}