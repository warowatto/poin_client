package com.payot_poin.poin.Page.Splash

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import com.payot_poin.poin.HashKey
import com.payot_poin.poin.Page.Login.LoginActivity
import com.payot_poin.poin.Page.RootActivity
import com.payot_poin.poin.R

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class SplashActivity : RootActivity() {

    val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
            BluetoothAdapter.getDefaultAdapter().enable()
        }

        ActivityCompat.requestPermissions(this, permissions, 2000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 2000 && grantResults.filter { it == PackageManager.PERMISSION_GRANTED }.size == permissions.size) {
            startActivity(Intent(this, LoginActivity::class.java))
            this.finish()
        }
    }
}