package com.payot_poin.poin.Page.Preference

/**
 * Created by yongheekim on 2018. 3. 12..
 */
interface AppPreferenceContract {

    interface View {

    }

    interface Presenter {
        fun autoBluetoothEnable(boolean: Boolean)
    }
}