package com.payot_poin.poin.Utils

import android.content.Context
import android.widget.Toast

/**
 * Created by yongheekim on 2018. 3. 11..
 */

infix fun String.toast(context: Context) = Toast.makeText(context, this, Toast.LENGTH_SHORT).show()