package com.payot_poin.poin.Page.User

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.payot_poin.poin.App
import com.payot_poin.poin.DI.Component.DaggerUserAccountComponent
import com.payot_poin.poin.DI.Component.DaggerUserComponent
import com.payot_poin.poin.DI.Module.Presenter.UserPresenter
import com.payot_poin.poin.R
import com.payot_poin.poin.Page.RootFragment
import kr.or.payot.poin.RESTFul.UserAPI
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 11..
 */
class AccountInfoFragment : RootFragment() {

    @Inject
    lateinit var presenter: UserContract.Presenter

    lateinit var btnUnRegistUser: CardView
    lateinit var btnLogoutUser: CardView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_user_info, container, false)

        btnLogoutUser = layout.findViewById(R.id.btnLogout)
        btnUnRegistUser = layout.findViewById(R.id.btnUnregistUser)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerUserAccountComponent.builder()
                .applicationComponent(App.component)
                .userPresenter(UserPresenter(this))
                .build().inject(this)

        btnLogoutUser.setOnClickListener {
            AlertDialog.Builder(activity)
                    .setTitle("로그아웃")
                    .setMessage("정말 로그아웃 하시겠습니까?")
                    .setPositiveButton("로그아웃") { dialog, _ -> presenter.logout() }
                    .setNegativeButton("닫기") { dialog, _ -> dialog.dismiss() }
                    .show()
        }
        btnUnRegistUser.setOnClickListener {
            AlertDialog.Builder(activity)
                    .setTitle("회원탈퇴")
                    .setMessage("회원을 탈퇴하시면 모든 정보를 잃게됩니다")
                    .setPositiveButton("회원탈퇴") { dialog, _ ->
                        val userId = App.user?.id
                        presenter.unregistUser(userId!!)
                    }
                    .setNegativeButton("닫기") { dialog, _ -> dialog.dismiss() }
                    .show()


        }
    }
}