package com.payot_poin.poin.Page.User

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerUserComponent
import com.payot_poin.poin.R
import com.payot_poin.poin.Page.RootFragment
import com.payot_poin.poin.Utils.convert
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.or.payot.poin.RESTFul.Data.Purchase
import kr.or.payot.poin.RESTFul.UserAPI
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class PaymentListFragment : RootFragment() {

    @Inject
    lateinit var userAPI: UserAPI

    lateinit var recyclerView: RecyclerView
    private lateinit var adapter: mAdapter

    private lateinit var errorGroup: ViewGroup

    private var items: List<Purchase>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.recycler_view, container, false)

        recyclerView = layout.findViewById(R.id.recyclerview)

        errorGroup = layout.findViewById(R.id.groupError)
        adapter = mAdapter()

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerUserComponent.builder()
                .applicationComponent(App.component)
                .build().inject(this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = mAdapter()
        }

        userAPI.list(App.user?.id!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it.isNotEmpty()) {
                                items = it
                                adapter.notifyDataSetChanged()
                            } else {
                                errorGroup.visibility = View.VISIBLE

                                val text = errorGroup.findViewById<TextView>(R.id.txtError)
                                val button = errorGroup.findViewById<Button>(R.id.btnError)

                                button.visibility = View.GONE

                                text.text = "Poin으로 자판기를 활용해 보세요~"
                            }
                        },
                        {
                            errorGroup.visibility = View.VISIBLE

                            val text = errorGroup.findViewById<TextView>(R.id.txtError)
                            val button = errorGroup.findViewById<Button>(R.id.btnError)

                            button.visibility = View.GONE

                            text.text = "Poin으로 자판기를 활용해 보세요~"
                        }
                )
    }

    private inner class mAdapter : RecyclerView.Adapter<mViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
            val inflater = LayoutInflater.from(parent?.context)
            val layout = inflater.inflate(R.layout.listitem_sell, parent, false)

            return mViewHolder(layout)
        }

        override fun getItemCount(): Int = items?.size ?: 0

        override fun onBindViewHolder(holder: mViewHolder, position: Int) {
            val item = items?.get(position)!!

            holder.run {
                txtProductName.text = item.productName
                txtPrice.text = "${item.amount} 원"
                txtAddress.text = item.machineName
                txtDate.text = item.pay_at.convert()
            }
        }

    }

    private class mViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtProductName: TextView
        val txtPrice: TextView
        val txtAddress: TextView
        val txtDate: TextView

        init {
            txtProductName = view.findViewById(R.id.txtSellProductName)
            txtPrice = view.findViewById(R.id.txtSellPrice)
            txtAddress = view.findViewById(R.id.txtSellMachineAddress)
            txtDate = view.findViewById(R.id.txtSellDate)
        }
    }
}