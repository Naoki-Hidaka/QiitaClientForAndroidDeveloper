package com.example.qiitaclient.presentation.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qiitaclient.databinding.FragmentArticleListBinding
import com.example.qiitaclient.databinding.ItemArticleListBinding
import com.example.qiitaclient.databinding.ItemTagBinding
import com.example.qiitaclient.presentation.viewmodel.ArticleListViewModel
import timber.log.Timber

class ArticleListFragment : Fragment() {

    private val viewModel: ArticleListViewModel by viewModels {
        ArticleListViewModel.Companion.Factory(
            activity?.application,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentArticleListBinding.inflate(inflater, container, false).let {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            Timber.d("debug: isLoading is $it")
        }
        it.lifecycleOwner = viewLifecycleOwner
        it.recyclerView.apply {
            adapter = ArticleListAdapter()
            layoutManager = LinearLayoutManager(context)
        }
        it.viewModel = viewModel
        it.root
    }

    inner class ArticleListAdapter : RecyclerView.Adapter<ArticleListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListViewHolder = ArticleListViewHolder(
            ItemArticleListBinding.inflate(LayoutInflater.from(context), parent, false)
        )

        override fun onBindViewHolder(holder: ArticleListViewHolder, position: Int) {
            holder.binding.let {
                it.lifecycleOwner = viewLifecycleOwner
                it.article = viewModel.articleList.value?.get(position)
                it.recyclerView.apply {
                    adapter = TagListAdapter(position)
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }
                it.constrainLayout.setOnClickListener {
                    val uri = Uri.parse(viewModel.articleList.value?.get(position)?.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
        }

        override fun getItemCount(): Int = viewModel.articleList.value?.size ?: 0
    }

    inner class TagListAdapter(private val itemPosition: Int) : RecyclerView.Adapter<TagListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagListViewHolder = TagListViewHolder(
            ItemTagBinding.inflate(LayoutInflater.from(context), parent, false)
        )

        override fun onBindViewHolder(holder: TagListViewHolder, position: Int) {
            holder.binding.let {
                it.lifecycleOwner = viewLifecycleOwner
                it.tag = viewModel.articleList.value?.get(itemPosition)?.tags?.get(position)?.name
            }
        }

        override fun getItemCount(): Int = viewModel.articleList.value?.get(itemPosition)?.tags?.size ?: 0
    }

    class ArticleListViewHolder(val binding: ItemArticleListBinding) : RecyclerView.ViewHolder(binding.root)

    class TagListViewHolder(val binding: ItemTagBinding) : RecyclerView.ViewHolder(binding.root)

}