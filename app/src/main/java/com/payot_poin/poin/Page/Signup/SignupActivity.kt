package com.payot_poin.poin.Page.Signup

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.payot_poin.poin.Page.RootActivity
import com.payot_poin.poin.R
import java.io.CharArrayWriter

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class SignupActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initView()
    }

    private fun initView() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_signup)
        setSupportActionBar(toolbar)

        val arrow = resources.getDrawable(R.drawable.abc_ic_ab_back_material)
        arrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(arrow)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> this.finish()
        }

        return true
    }
}