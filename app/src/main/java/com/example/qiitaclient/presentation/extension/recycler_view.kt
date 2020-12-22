package com.example.qiitaclient.presentation.extension

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qiitaclient.domain.model.ArticleWithTag
import com.example.qiitaclient.presentation.view.fragment.ArticleListFragment

@BindingAdapter("binding_listObserve")
@Suppress("UNUSED_PARAMETER")
fun RecyclerView.listObserve(observedList: Collection<Any>?) {
    adapter?.notifyDataSetChanged()
}

@BindingAdapter("binding_diffObserve")
fun RecyclerView.diffObserve(items: List<ArticleWithTag>?) {
    items?.let {
        (adapter as ArticleListFragment.ArticleListAdapter).submitList(it)
    }
}