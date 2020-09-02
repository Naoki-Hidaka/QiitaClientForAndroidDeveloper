package com.example.qiitaclient.presentation.extension

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("binding_toggle")
fun View.setVisibility(visibility: Boolean) {
    this.visibility = if(visibility) {
        View.VISIBLE
    } else {
        View.GONE
    }
}