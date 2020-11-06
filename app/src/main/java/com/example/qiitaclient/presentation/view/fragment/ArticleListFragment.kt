package com.example.qiitaclient.presentation.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qiitaclient.databinding.FragmentArticleListBinding
import com.example.qiitaclient.databinding.ItemArticleListBinding
import com.example.qiitaclient.databinding.ItemTagBinding
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.presentation.viewmodel.ArticleListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class ArticleListFragment : Fragment() {

    private val viewModel: ArticleListViewModel by viewModels {
        ArticleListViewModel.Companion.Factory(
            activity?.application,
            activity
        )
    }

    private val articlePagingSourceAdapter = ArticlePagingSourceAdapter(ArticleListComparator())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentArticleListBinding.inflate(inflater, container, false).let {
        it.lifecycleOwner = viewLifecycleOwner
        it.viewModel = viewModel
        lifecycleScope.launch {
            articlePagingSourceAdapter.run {
                loadStateFlow.collectLatest { loadState ->
                    it.progressBar.isVisible = loadState.refresh is LoadState.Loading
                }
            }
        }
        it.recyclerView.apply {
            adapter = articlePagingSourceAdapter.apply {
                lifecycleScope.launch {
                    viewModel.flow.collectLatest { pagingData ->
                        submitData(pagingData)
                    }
                }
            }
            layoutManager = LinearLayoutManager(context)
        }
        it.root
    }

    inner class ArticlePagingSourceAdapter(diffCallback: DiffUtil.ItemCallback<Article>) :
        PagingDataAdapter<Article, ArticleListViewHolder>(diffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListViewHolder =
            ItemArticleListBinding.inflate(layoutInflater, parent, false).let {
                ArticleListViewHolder(it)
            }

        override fun onBindViewHolder(holder: ArticleListViewHolder, position: Int) {
            holder.binding.let {
                it.lifecycleOwner = viewLifecycleOwner
                it.article = getItem(position)
                lifecycleScope.launch {
                    viewModel.flow
                }
                it.recyclerView.apply {
                    adapter = TagListAdapter(getItem(position))
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }
                it.constrainLayout.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getItem(position)?.url)))

                }
            }
        }
    }

    inner class TagListAdapter(private val article: Article?) :
        RecyclerView.Adapter<TagListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagListViewHolder =
            TagListViewHolder(
                ItemTagBinding.inflate(LayoutInflater.from(context), parent, false)
            )

        override fun onBindViewHolder(holder: TagListViewHolder, position: Int) {
            holder.binding.let {
                it.lifecycleOwner = viewLifecycleOwner
                it.tag = article?.tags?.get(position)?.name
            }
        }

        override fun getItemCount(): Int = article?.tags?.size ?: 0
    }


    class ArticleListViewHolder(val binding: ItemArticleListBinding) :
        RecyclerView.ViewHolder(binding.root)

    class TagListViewHolder(val binding: ItemTagBinding) : RecyclerView.ViewHolder(binding.root)

}

class ArticleListComparator : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.id == newItem.id
}