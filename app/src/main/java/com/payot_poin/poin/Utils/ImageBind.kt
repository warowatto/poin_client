package com.payot_poin.poin.Utils

import android.widget.ImageView
import com.payot_poin.poin.R
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

/**
 * Created by yongheekim on 2018. 3. 11..
 */
fun ImageView.href(path: String, defaultImage: Int = R.drawable.ic_user_profile) {
    Picasso.with(this.context).load(path).transform(CropCircleTransformation()).placeholder(defaultImage).into(this)
}