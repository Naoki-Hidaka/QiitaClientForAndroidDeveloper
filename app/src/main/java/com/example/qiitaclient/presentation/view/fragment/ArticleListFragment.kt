package com.example.qiitaclient.presentation.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qiitaclient.databinding.FragmentArticleListBinding
import com.example.qiitaclient.databinding.ItemArticleListBinding
import com.example.qiitaclient.databinding.ItemTagBinding
import com.example.qiitaclient.domain.model.ArticleWithTag
import com.example.qiitaclient.domain.model.Tag
import com.example.qiitaclient.domain.service.MyApplication
import com.example.qiitaclient.presentation.viewmodel.ArticleListViewModel

class ArticleListFragment : Fragment() {

    private val viewModel: ArticleListViewModel by viewModels {
        ArticleListViewModel.Companion.Factory(
            activity?.application,
            (activity?.application as MyApplication).provideArticleListRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentArticleListBinding.inflate(inflater, container, false).let {
        it.lifecycleOwner = viewLifecycleOwner
        it.recyclerView.apply {
            adapter = ArticleListAdapter()
            layoutManager = LinearLayoutManager(context)
        }
        it.viewModel = viewModel
        it.root
    }

    inner class ArticleListAdapter :
        ListAdapter<ArticleWithTag, ArticleListViewHolder>(ArticleDiffCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListViewHolder =
            ArticleListViewHolder(
                ItemArticleListBinding.inflate(LayoutInflater.from(context), parent, false)
            )

        override fun onBindViewHolder(holder: ArticleListViewHolder, position: Int) {
            holder.binding.let {
                it.lifecycleOwner = viewLifecycleOwner
                it.article = getItem(position).article
                it.recyclerView.apply {
                    adapter = TagListAdapter().apply { submitList(getItem(position).tags) }
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }
                it.constrainLayout.setOnClickListener {
                    val uri = Uri.parse(getItem(position).article.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<ArticleWithTag>() {
        override fun areItemsTheSame(oldItem: ArticleWithTag, newItem: ArticleWithTag): Boolean =
            oldItem.article.id == newItem.article.id

        override fun areContentsTheSame(oldItem: ArticleWithTag, newItem: ArticleWithTag): Boolean =
            oldItem == newItem

    }

    inner class TagListAdapter :
        ListAdapter<Tag, TagListViewHolder>(TagDiffCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagListViewHolder =
            TagListViewHolder(
                ItemTagBinding.inflate(LayoutInflater.from(context), parent, false)
            )

        override fun onBindViewHolder(holder: TagListViewHolder, position: Int) {
            holder.binding.let {
                it.lifecycleOwner = viewLifecycleOwner
                it.tag = getItem(position).name
            }
        }
    }

    class ArticleListViewHolder(val binding: ItemArticleListBinding) :
        RecyclerView.ViewHolder(binding.root)

    class TagListViewHolder(val binding: ItemTagBinding) : RecyclerView.ViewHolder(binding.root)

    class TagDiffCallback : DiffUtil.ItemCallback<Tag>() {
        override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean =
            oldItem == newItem
    }

}