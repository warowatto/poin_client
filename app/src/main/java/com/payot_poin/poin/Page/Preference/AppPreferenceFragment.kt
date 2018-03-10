package com.payot_poin.poin.Page.Preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.payot_poin.poin.R
import com.payot_poin.poin.Page.RootFragment

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class AppPreferenceFragment : RootFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_app_config, container, false)

        return layout
    }
}