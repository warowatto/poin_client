package com.payot_poin.poin.Interface

import android.bluetooth.BluetoothDevice
import io.reactivex.Observable

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface DeviceScanner {

    fun scan(): Observable<BluetoothDevice>

    fun stopScan()
}