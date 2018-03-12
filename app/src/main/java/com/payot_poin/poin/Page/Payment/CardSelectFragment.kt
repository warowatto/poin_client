package com.payot_poin.poin.Page.Payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.payot_poin.poin.Page.RootFragment
import com.payot_poin.poin.R
import com.payot_poin.poin.Utils.convert
import kotlinx.android.synthetic.main.listitem_card.*
import kr.or.payot.poin.RESTFul.Data.Card

/**
 * Created by yongheekim on 2018. 3. 12..
 */
class CardSelectFragment : RootFragment() {

    companion object {
        fun newInstance(card: Card): CardSelectFragment {
            val args = Bundle()
            args.putParcelable("card", card)

            val fragment = CardSelectFragment()
            fragment.arguments = args

            return fragment
        }
    }

    lateinit var txtCardName: TextView
    lateinit var txtCardBank: TextView
    lateinit var txtCardCreateAt: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.listitem_card, container, false)

        txtCardName = layout.findViewById(R.id.txtCardName)
        txtCardBank = layout.findViewById(R.id.txtBankName)
        txtCardCreateAt = layout.findViewById(R.id.txtCreateAt)

        layout.findViewById<ImageView>(R.id.btnCardDelete).visibility = View.GONE

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val card = arguments?.getParcelable<Card>("card")!!

        txtCardName.text = card.displayName
        txtBankName.text = card.bankName
        txtCardCreateAt.text = card.create_at.convert()

    }
}