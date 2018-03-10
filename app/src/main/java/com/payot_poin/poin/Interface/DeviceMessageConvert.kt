package com.payot_poin.poin.Interface

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface DeviceMessageConvert {

    fun sendMessage(message: String): ByteArray

    fun reciveMessage(message: ByteArray): List<String>

    fun crc(byteArray: ByteArray): ByteArray
}