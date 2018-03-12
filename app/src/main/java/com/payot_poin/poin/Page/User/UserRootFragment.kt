package com.payot_poin.poin.Page.User

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.payot_poin.poin.R
import com.payot_poin.poin.Page.RootFragment

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class UserRootFragment : RootFragment() {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_user_main, container, false)

        tabLayout = layout.findViewById(R.id.tablayout_user)
        viewPager = layout.findViewById(R.id.viewpager_user)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        viewPager.adapter = UserViewPagerAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_card)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_list)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_user_profile)
    }

    class UserViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        val titles = arrayOf("내카드", "사용내역", "계정정보")
        val pages = arrayOf(HaveCardFragment(), PaymentListFragment(), AccountInfoFragment())

        override fun getItem(position: Int): Fragment = pages[position]

        override fun getCount(): Int = pages.size
    }
}