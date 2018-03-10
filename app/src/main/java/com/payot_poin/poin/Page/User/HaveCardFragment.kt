package com.payot_poin.poin.Page.User

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.payot_poin.poin.Page.User.Adapter.CardListAdapter
import com.payot_poin.poin.R
import com.payot_poin.poin.Page.RootFragment

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class HaveCardFragment : RootFragment() {

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.recycler_view, container, false)

        recyclerView = layout.findViewById(R.id.recyclerview)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = CardListAdapter()
        }
    }
}