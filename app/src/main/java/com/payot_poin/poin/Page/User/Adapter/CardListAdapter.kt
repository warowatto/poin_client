package com.payot_poin.poin.Page.User.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.payot_poin.poin.R

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class CardListAdapter : RecyclerView.Adapter<CardListAdapter.CardListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val layout = inflater.inflate(R.layout.listitem_card, parent, false)

        return CardListViewHolder(layout)
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {

    }

    class CardListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}