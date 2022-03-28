package com.example.qiitaclient.presentation.extension

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
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

@BindingAdapter("onScrollEnd")
fun RecyclerView.setOnScrollEnd(
    onScrollEnd: RecyclerViewExt.OnScrollEnd?,
) {
    addOnScrollListener(
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val llm = layoutManager as? LinearLayoutManager

                llm?.let {
                    val visibleItemCount = it.childCount
                    val totalItemCount = it.itemCount
                    val pastVisibleItems = it.findFirstVisibleItemPosition()

                    if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                        onScrollEnd?.onScrollEnd()
                    }
                }
            }
        }
    )
}

class RecyclerViewExt {
    interface OnScrollEnd {
        fun onScrollEnd()
    }
}
