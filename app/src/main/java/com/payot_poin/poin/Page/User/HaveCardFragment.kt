package com.payot_poin.poin.Page.User

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerUserComponent
import com.payot_poin.poin.DI.Module.Presenter.UserPresenter
import com.payot_poin.poin.Page.CardAdd.CardAddActivity
import com.payot_poin.poin.R
import com.payot_poin.poin.Page.RootFragment
import com.payot_poin.poin.Utils.convert
import com.payot_poin.poin.Utils.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.recycler_view.*
import kr.or.payot.poin.RESTFul.Data.Card
import kr.or.payot.poin.RESTFul.UserAPI
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class HaveCardFragment : RootFragment() {

    @Inject
    lateinit var userAPI: UserAPI

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: mAdapter

    private lateinit var errorGroup: ViewGroup
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.recycler_view, container, false)

        recyclerView = layout.findViewById(R.id.recyclerview)
        errorGroup = layout.findViewById(R.id.groupError)
        fab = layout.findViewById(R.id.fab)

        adapter = mAdapter()

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerUserComponent.builder()
                .applicationComponent(App.component)
                .build().inject(this)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        fab.visibility = View.VISIBLE
        fab.setOnClickListener {
            startActivity(Intent(activity, CardAddActivity::class.java))
        }

        if (App.user?.cards?.isEmpty() ?: true) {
            errorGroup.visibility = View.VISIBLE

            val image = errorGroup.findViewById<ImageView>(R.id.imgError)
            val text = errorGroup.findViewById<TextView>(R.id.txtError)
            val button = errorGroup.findViewById<Button>(R.id.btnError)

            text.text = "등록된 카드가 없어요 :("
            button.text = "등록하기"
            button.setOnClickListener {
                val intent = Intent(activity, CardAddActivity::class.java)
                activity?.startActivity(intent)
            }
        } else {
            errorGroup.visibility = View.GONE
        }

        adapter.notifyDataSetChanged()
    }

    fun removeCard(card: Card) {
        AlertDialog.Builder(this.context!!)
                .setTitle("카드삭제")
                .setMessage("카드를 정말로 삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, _ ->
                    val userId = App.user?.id!!
                    userAPI.removeCard(userId, card.id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        App.user?.cards?.remove(card)
                                        adapter.notifyDataSetChanged()
                                        dialog.dismiss()
                                        "카드가 삭제되었습니다" toast this.context!!
                                    },
                                    {
                                        it.printStackTrace()
                                    }
                            )
                }
                .setNegativeButton("닫기") { dialog, _ -> dialog.dismiss() }
                .setOnDismissListener { adapter.notifyDataSetChanged() }
                .show()
    }

    private inner class mAdapter : RecyclerView.Adapter<mViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
            val inflater = LayoutInflater.from(parent?.context)
            val layout = inflater.inflate(R.layout.listitem_card, parent, false)

            return mViewHolder(layout)
        }

        override fun getItemCount(): Int = App.user?.cards?.size ?: 0

        override fun onBindViewHolder(holder: mViewHolder, position: Int) {
            val card = App.user?.cards?.get(position)!!

            holder.run {
                txtName.text = card.displayName
                txtBankName.text = card.bankName
                txtCreateAt.text = card.create_at.convert()
                imgDelete.setOnClickListener { removeCard(card) }
            }

        }

    }

    private class mViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView
        val txtBankName: TextView
        val txtCreateAt: TextView
        val imgDelete: ImageButton

        init {
            txtName = view.findViewById(R.id.txtCardName)
            txtBankName = view.findViewById(R.id.txtBankName)
            txtCreateAt = view.findViewById(R.id.txtCreateAt)
            imgDelete = view.findViewById(R.id.btnCardDelete)
        }
    }
}