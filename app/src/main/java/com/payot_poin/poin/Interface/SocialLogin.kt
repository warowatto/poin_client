package com.payot_poin.poin.Interface

import io.reactivex.Observable

/**
 * Created by yongheekim on 2018. 3. 11..
 */
interface SocialLogin {
    fun login(flatform: String): Observable<Map<String, String>>
}