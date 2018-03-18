package com.payot_poin.poin.DI.Module

import android.bluetooth.*
import android.content.Context
import com.payot_poin.poin.DI.PerActivity
import com.payot_poin.poin.DI.PerFragment
import com.payot_poin.poin.Interface.DeviceController
import com.payot_poin.poin.Interface.DeviceMessageConvert
import com.payot_poin.poin.Interface.DeviceMessageEncryption
import com.payot_poin.poin.Interface.DeviceScanner
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Singleton

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Module
class PoinDeviceModule {

    @PerFragment
    @Provides
    fun provideDeviceScanner(): DeviceScanner = object : DeviceScanner {

        lateinit var leScanCallback: BluetoothAdapter.LeScanCallback

        override fun scan(): Single<BluetoothDevice> {
            return Single.create { emitter ->
                leScanCallback = BluetoothAdapter.LeScanCallback { device, _, _ ->
                    val deviceName = device.name
                    val macAddress = device.address

                    if (deviceName == macAddress.replace(":", "")) {
                        emitter.onSuccess(device)
                        stopScan()
                    }
                }

                BluetoothAdapter.getDefaultAdapter().startLeScan(leScanCallback)
            }
        }

        override fun stopScan() {
            BluetoothAdapter.getDefaultAdapter().stopLeScan(leScanCallback)
        }

    }

    @PerActivity
    @Provides
    fun provideMessageencryption(): DeviceMessageEncryption = object : DeviceMessageEncryption {
        private val key = byteArrayOf(
                0x2B, 0x7E, 0x15, 0x16, 0x28,
                0xAE.toByte(), 0xD2.toByte(), 0xA6.toByte(), 0xAB.toByte(), 0xF7.toByte(),
                0x15, 0x88.toByte(), 0x09, 0xCF.toByte(), 0x4F,
                0x3C)

        private val iv = byteArrayOf(
                0x00, 0x01, 0x02, 0x03, 0x04,
                0x05, 0x06, 0x07, 0x08, 0x09,
                0x0A, 0x0B, 0x0C, 0x0D, 0x0E,
                0x0F)

        private val cipher: Cipher = Cipher.getInstance("AES/CBC/NoPadding")
        private val keySpec = SecretKeySpec(key, "AES")
        private val ivSpec = IvParameterSpec(iv)

        override fun encrypt(byteArray: ByteArray): ByteArray {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

            return cipher.doFinal(byteArray)
        }

        override fun decrpyt(byteArray: ByteArray): ByteArray {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

            return cipher.doFinal(byteArray)
        }
    }

    @PerActivity
    @Provides
    fun provideDeviceMessageConvert(messageEncryption: DeviceMessageEncryption): DeviceMessageConvert = object : DeviceMessageConvert {
        override fun sendMessage(message: String): ByteArray {
            val messageBytes = message.toByteArray()

            val totalMessageSize = 64
            val messageSize = messageBytes.size
            val messageCheckSumSize = 2
            val firstRandomByteSize = 4
            val lastDummyRandomByteSize = totalMessageSize - firstRandomByteSize - messageSize - messageCheckSumSize

            val totalEncryptionMessage = byteArrayOf(
                    *randomByteCreate(firstRandomByteSize),
                    *messageBytes,
                    *crc(messageBytes),
                    *randomByteCreate(lastDummyRandomByteSize))

            return messageEncryption.encrypt(totalEncryptionMessage)

        }

        override fun reciveMessage(message: ByteArray): List<String> {
            val plainTextBytes = messageEncryption.decrpyt(message)

            val checkedCRCMessage = plainTextBytes.drop(4)
            var responseMessageBytes: ByteArray? = null

            checksum@
            for ((index, _) in checkedCRCMessage.withIndex()) {
                val validationMessage = checkedCRCMessage.take(index + 1).toByteArray()
                if (crc(validationMessage) contentEquals byteArrayOf(0x00, 0x00)) {
                    responseMessageBytes = validationMessage.dropLast(2).toByteArray()
                    break@checksum
                }
            }

            if (responseMessageBytes != null) {
                return responseMessageBytes.toString().split(" ")
            } else {
                throw NullPointerException("Message Format Error")
            }
        }

        override fun crc(byteArray: ByteArray): ByteArray {
            var crc = 0xffff
            var flag: Int

            for (b in byteArray) {
                crc = crc xor (b.toInt() and 0xff)

                for (i in 0..7) {
                    flag = crc and 1
                    crc = crc shr 1
                    if (flag != 0) {
                        crc = crc xor 0xa001
                    }
                }
            }

            val buffer = ByteBuffer.allocate(2)
            buffer.order(ByteOrder.LITTLE_ENDIAN)

            while (buffer.hasRemaining()) {
                buffer.putShort(crc.toShort())
            }

            return buffer.array()
        }

        fun randomByteCreate(size: Int): ByteArray = SecureRandom.getSeed(size)

    }

    @PerActivity
    @Provides
    fun provideDeviceController(messageConvert: DeviceMessageConvert): DeviceController = object : BluetoothGattCallback(), DeviceController {

        var gatt: BluetoothGatt? = null
        var gattChar: BluetoothGattCharacteristic? = null

        val statusObserver = PublishSubject.create<Int>()
        val responseObserver = PublishSubject.create<Byte>()
        val response = responseObserver.buffer(64).map { it.toByteArray() }

        override fun connect(context: Context, bluetoothDevice: BluetoothDevice): Observable<Int> {
            bluetoothDevice.connectGatt(context, false, this)

            return statusObserver
        }

        override fun sendMessage(message: String): Observable<List<String>> {
            val messageByte = messageConvert.sendMessage(message)

            val messageSliceObserver = messageByte.toObservable().buffer(20).map { it.toByteArray() }
            val messageDelayOffset = Observable.interval(0, 50L, TimeUnit.MILLISECONDS, Schedulers.computation())

            return messageSliceObserver.zipWith(messageDelayOffset) { bytes, _ -> bytes }
                    .doOnNext {
                        bluetoothMessageSend(it)
                    }.takeLast(1)
                    .flatMap { responseObserver.buffer(64).map { it.toByteArray() } }
                    .take(1)
                    .map { messageConvert.reciveMessage(it) }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            val findService = findChar(gatt!!)
            this.gatt = gatt
            this.gattChar = findService?.second!!

            if (findService != null && this.gatt?.setCharacteristicNotification(findService.second, true) ?: false) {
                statusObserver.onNext(1000)
            }
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            statusObserver.onNext(newState)

            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> gatt?.discoverServices()
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)

            characteristic?.value?.let { it.forEach { responseObserver.onNext(it) } }
        }

        fun bluetoothMessageSend(byteArray: ByteArray) {
            gattChar?.value = byteArray
            gatt?.writeCharacteristic(gattChar)
        }

        override fun disconnect() {
            sendMessage("CMD S 3")
                    .subscribe { gatt?.close() }
        }

        fun findChar(bluetoothGatt: BluetoothGatt): Pair<BluetoothGattService, BluetoothGattCharacteristic>? {
            for (gattService in bluetoothGatt.services) {
                for (gattChar in gattService.characteristics) {
                    if (isWriteable(gattChar) && isNotify(gattChar)) {
                        return gattService to gattChar
                    }
                }
            }

            return null
        }

        fun isWriteable(characteristic: BluetoothGattCharacteristic): Boolean {
            return (characteristic.properties and (BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0
        }

        fun isNotify(characteristic: BluetoothGattCharacteristic): Boolean {
            return (characteristic.properties and (BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0)
        }

    }
}