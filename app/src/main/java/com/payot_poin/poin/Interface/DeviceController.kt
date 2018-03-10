package com.payot_poin.poin.Interface

import android.bluetooth.BluetoothDevice
import android.content.Context
import io.reactivex.Observable

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface DeviceController {

    fun connect(context: Context, bluetoothDevice: BluetoothDevice): Observable<Int>

    fun sendMessage(message: String): Observable<List<String>>

    fun disconnect()
}