package com.example.qiitaclient.presentation.extension

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("binding_listObserve")
@Suppress("UNUSED_PARAMETER")
fun RecyclerView.listObserve(observedList: Collection<Any>?) {
    adapter?.notifyDataSetChanged()
}