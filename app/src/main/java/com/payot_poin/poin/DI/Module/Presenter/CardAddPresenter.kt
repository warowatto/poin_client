package com.payot_poin.poin.DI.Module.Presenter

import com.payot_poin.poin.App
import com.payot_poin.poin.DI.PerActivity
import com.payot_poin.poin.Page.CardAdd.CardAddActivity
import com.payot_poin.poin.Page.CardAdd.CardAddContract
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.or.payot.poin.RESTFul.UserAPI
import retrofit2.HttpException

/**
 * Created by yongheekim on 2018. 3. 11..
 */

@Module
class CardAddPresenter(val activity: CardAddActivity) {

    @PerActivity
    @Provides
    fun providePresenter(userAPI: UserAPI): CardAddContract.Presenter = object : CardAddContract.Presenter {

        lateinit var view: CardAddContract.View

        override fun attachView(view: CardAddContract.View) {
            this.view = view
        }

        override fun addCard(name: String, number: String, expiry: String, pass2digit: String, birth: String) {

            val user = App.user

            view.cardAddProgress()
            userAPI.addCard(user!!.id, name, number, expiry, birth, pass2digit)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnEvent { _, _ -> view.endCardAddProgress() }
                    .subscribe(
                            {
                                user?.cards?.add(it)
                                view.sucess(it)
                            },
                            {
                                if (it is HttpException) {
                                    val message = it.response().message().toString()
                                    view.cardErrorError(message)
                                } else {
                                    view.cardErrorError("알 수 없는 오류가 발생하였습니다")
                                }
                            }
                    )
        }

    }
}