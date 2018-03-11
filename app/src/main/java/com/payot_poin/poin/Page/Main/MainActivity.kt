package com.payot_poin.poin.Page.Main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.widget.TextView
import com.payot_poin.poin.App
import com.payot_poin.poin.Page.CardAdd.CardAddFragment
import com.payot_poin.poin.Page.Preference.AppPreferenceFragment
import com.payot_poin.poin.Page.RootActivity
import com.payot_poin.poin.Page.Scan.ScanFragment
import com.payot_poin.poin.Page.User.UserRootFragment
import com.payot_poin.poin.R

/**
 * Created by yongheekim on 2018. 3. 10..
 */
class MainActivity : RootActivity() {

    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

    }

    private fun initView() {
        val tablayout: TabLayout = findViewById(R.id.tablayout_main)
        viewPager = findViewById(R.id.viewpager_main)

        viewPager.adapter = MainPagerAdapter(supportFragmentManager)
        tablayout.setupWithViewPager(viewPager)

        tablayout.getTabAt(0)?.setIcon(R.drawable.ic_camera)
        tablayout.getTabAt(1)?.setIcon(R.drawable.ic_person)
        tablayout.getTabAt(2)?.setIcon(R.drawable.ic_setting)

        val userNameTextView = findViewById<TextView>(R.id.txtUserName)

        userNameTextView.text = "${App.user?.id}님 반갑습니다"
    }

    class MainPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        val pages = arrayOf(ScanFragment(), UserRootFragment(), AppPreferenceFragment())
        val title = arrayOf("QR코드", "내정보", "카드등록", "설정")

        override fun getItem(position: Int): Fragment = pages[position]

        override fun getCount(): Int = pages.size

        // override fun getPageTitle(position: Int): CharSequence? = title[position]

    }

}