package com.payot_poin.poin.Utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by yongheekim on 2018. 3. 11..
 */
fun Date.convert(): String {
    val format = SimpleDateFormat("yyyy.MM.dd")

    return format.format(this)
}