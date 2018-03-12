package com.payot_poin.poin.Page.Preference

import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerPreferenceComponent
import com.payot_poin.poin.DI.Module.Presenter.AppPreferencePresenter
import com.payot_poin.poin.Page.RootFragment
import com.payot_poin.poin.R
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class AppPreferenceFragment : RootFragment() {

    @Inject
    lateinit var presenter: AppPreferenceContract.Presenter

    lateinit var switchGroup: CardView
    lateinit var switch: Switch

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_app_config, container, false)

        switch = layout.findViewById(R.id.switchBluetooth)
        switchGroup = layout.findViewById(R.id.cardBluetooth)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerPreferenceComponent.builder()
                .applicationComponent(App.component)
                .appPreferencePresenter(AppPreferencePresenter())
                .build().inject(this)


        switchGroup.setOnClickListener {
            val toogleState = switch.isChecked
            switch.isChecked = !toogleState
        }
        switch.setOnCheckedChangeListener { _, isEnable -> presenter.autoBluetoothEnable(isEnable) }
    }
}