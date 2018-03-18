package com.payot_poin.poin.Page.Main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.widget.ImageView
import android.widget.TextView
import com.payot_poin.poin.App
import com.payot_poin.poin.Page.Preference.AppPreferenceFragment
import com.payot_poin.poin.Page.RootActivity
import com.payot_poin.poin.Page.Scan.ScanFragment
import com.payot_poin.poin.Page.User.UserRootFragment
import com.payot_poin.poin.R
import com.payot_poin.poin.Utils.href
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by yongheekim on 2018. 3. 10..
 */
class MainActivity : RootActivity() {

    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private fun initView() {
        val tablayout: TabLayout = findViewById(R.id.tablayout_main)
        viewPager = findViewById(R.id.viewpager_main)
        viewPager.offscreenPageLimit = 3

        viewPager.adapter = MainPagerAdapter(supportFragmentManager)
        tablayout.setupWithViewPager(viewPager)

        tablayout.getTabAt(0)?.setIcon(R.drawable.ic_camera)
        tablayout.getTabAt(1)?.setIcon(R.drawable.ic_person)
        tablayout.getTabAt(2)?.setIcon(R.drawable.ic_setting)

        val userNameTextView = findViewById<TextView>(R.id.txtUserName)
        val userDescriptionView = findViewById<TextView>(R.id.txtUserDescription)
        val profileImage = findViewById<ImageView>(R.id.imgProfile)

        userNameTextView.text = "${App.user?.name}님 안녕하세요"
        txtUserDescription.text = App.user?.email
        App.user?.profileImage?.let {
            profileImage.href(it)
        }

        txtTitle.isSelected = true

    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    class MainPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        val pages = arrayOf(ScanFragment(), UserRootFragment(), AppPreferenceFragment())
        val title = arrayOf("QR코드", "내정보", "카드등록", "설정")

        override fun getItem(position: Int): Fragment = pages[position]

        override fun getCount(): Int = pages.size


    }

}