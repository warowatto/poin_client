package com.payot_poin.poin.Page.Payment

import kr.or.payot.poin.RESTFul.Data.Card
import kr.or.payot.poin.RESTFul.Data.Company
import kr.or.payot.poin.RESTFul.Data.Machine
import kr.or.payot.poin.RESTFul.Data.Product

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface PaymentContract {

    interface View {
        fun showProduct(products: List<Product>?)
        fun showCompanyInfo(company: Company)
        fun showDeviceInfo(machine: Machine)
        fun paymentSuccess()
        fun errorAuthDevice()
        fun paymentError(message: String)
        fun progressPayment()
        fun endProgressPayment()
        fun progressToService()
        fun readyToService()

    }

    interface Presenter {
        fun attachview(view: PaymentContract.View)

        fun payment(product: Product, card: Card, point: Int)
    }
}