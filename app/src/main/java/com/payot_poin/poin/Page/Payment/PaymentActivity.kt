package com.payot_poin.poin.Page.Payment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerPaymentComponent
import com.payot_poin.poin.DI.Module.Presenter.PaymentPresenter
import com.payot_poin.poin.Page.RootActivity
import com.payot_poin.poin.R
import com.payot_poin.poin.Utils.toast
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.bottom_payment.*
import kr.or.payot.poin.RESTFul.Data.Company
import kr.or.payot.poin.RESTFul.Data.Machine
import kr.or.payot.poin.RESTFul.Data.Product
import kr.or.payot.poin.RESTFul.MachineResponse
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class PaymentActivity : RootActivity(), PaymentContract.View {


    @Inject
    lateinit var presenter: PaymentContract.Presenter

    lateinit var bottomSheet: BottomSheetBehavior<View>

    val progressServiceReady:ProgressDialog by lazy {
        ProgressDialog(this).apply {
            setMessage("서비스를 준비중입니다 잠시만 기다려 주십시오")
            setCancelable(false)
        }
    }
    val progress: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            setTitle("결제가 진행중 입니다")
            setMessage("앱을 종료하시지 말아 주세요 :)")
            setCancelable(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        bottomSheet = BottomSheetBehavior.from(bottom_sheet)

        DaggerPaymentComponent.builder()
                .applicationComponent(App.component)
                .paymentPresenter(PaymentPresenter(this))
                .build().inject(this)

        presenter.attachview(this)
    }

    override fun progressToService() {
        progressServiceReady.show()
    }

    override fun readyToService() {
        progressServiceReady.dismiss()
    }

    override fun showProduct(products: List<Product>?) {
        recyclerview_payment.layoutManager = LinearLayoutManager(this)
        recyclerview_payment.adapter = mAdapter(products!!)
        recyclerview_payment.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun showCompanyInfo(company: Company) {
        txtCompanyName.text = company.name
        txtCompanyEmail.text = company.email
        txtCompanyTel.text = company.tel
    }

    override fun showDeviceInfo(machine: Machine) {
        txtMachineType.text = machine.type
        txtMachineAddress.text = machine.displayName
        txtMachineDescription.text = machine.description
    }

    override fun paymentSuccess() {
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

        val machine: MachineResponse = intent.extras.getParcelable("device")

        AlertDialog.Builder(this)
                .setTitle("결제 완료")
                .setMessage("결제가 성공적으로 완료되었습니다\n")
                .setNeutralButton("확인") { dialog, _ -> dialog.dismiss() }
                .show()
    }

    override fun errorAuthDevice() {

    }

    override fun paymentError(message: String) {
        AlertDialog.Builder(this)
                .setTitle("결제 오류")
                .setMessage(message)
                .setNeutralButton("확인") { dialog, _ -> dialog.dismiss() }
                .show()
    }

    override fun progressPayment() {
        progress.show()
    }

    override fun endProgressPayment() {
        progress.dismiss()
    }

    fun showPayment(product: Product) {
        val machine = intent.extras.getParcelable("device") as MachineResponse
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED

        viewpager_payment.adapter = PagerAdapter()
        txtBottomProductName.text = product.name
        txtBottomDevicePosition.text = machine.machine.displayName
        txtBottomPoint.text = App.user?.point?.toString()
        txtBottomPrice.text = product.price.toString()

        imgBottomClose.setOnClickListener {
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        }

        if (product.price > App.user?.point ?: 0) {
            btnBottomPayPoint.visibility = View.GONE
        } else {
            btnBottomPayPoint.visibility = View.VISIBLE
        }

        // 동의

        btnBottomPayCard.setOnClickListener {

            if (!checkAgree.isChecked) {
                "이용약관에 동의하셔야 결제가 진행됩니다" toast this
            }

            val card = App.user?.cards?.get(viewpager_payment.currentItem)!!

            AlertDialog.Builder(this)
                    .setTitle("카드결제하기")
                    .setMessage("선택하신 카드가 ${card.displayName}(${card.bankName})이\n맞으시면 결제하기를 진행해 주세요")
                    .setNegativeButton("닫기") { dialog, _ -> dialog.dismiss()}
                    .setPositiveButton("결제하기") { dialog, _ -> presenter.payment(product, card, 0) }
                    .show()
        }

        btnBottomPayPoint.setOnClickListener {
            val card = App.user?.cards?.get(viewpager_payment.currentItem)!!
            presenter.payment(product, card, product.price)
        }
    }

    private inner class mAdapter(val items: List<Product>) : RecyclerView.Adapter<mViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val layout = inflater.inflate(R.layout.listitem_product, parent, false)

            return mViewHolder(layout)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: mViewHolder, position: Int) {
            val item = items[position]

            holder.run {
                txtProductName.text = item.name
                txtProductDescription.text = item.description
                txtPrice.text = "${item.price} 원"
                rootView.setOnClickListener { showPayment(item) }
            }
        }
    }

    private class mViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rootView: View
        val txtProductName: TextView
        val txtProductDescription: TextView
        val txtPrice: TextView

        init {
            rootView = view
            txtProductName = view.findViewById(R.id.txtProductName)
            txtProductDescription = view.findViewById(R.id.txtProducDescription)
            txtPrice = view.findViewById(R.id.txtProductPrice)
        }
    }

    private inner class PagerAdapter : FragmentPagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
             val card = App.user?.cards?.get(position)
            return CardSelectFragment.newInstance(card!!)
        }

        override fun getCount(): Int = App.user?.cards?.size ?: 0

    }

}