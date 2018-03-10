package com.payot_poin.poin.Interface

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface DeviceMessageEncryption {

    fun encrypt(byteArray: ByteArray): ByteArray

    fun decrpyt(byteArray: ByteArray): ByteArray
}