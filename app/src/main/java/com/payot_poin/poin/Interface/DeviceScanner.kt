package com.payot_poin.poin.Interface

import android.bluetooth.BluetoothDevice
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface DeviceScanner {

    fun scan(): Single<BluetoothDevice>

    fun stopScan()
}