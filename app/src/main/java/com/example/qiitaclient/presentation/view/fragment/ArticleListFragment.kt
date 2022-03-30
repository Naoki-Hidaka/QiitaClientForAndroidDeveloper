package com.example.qiitaclient.presentation.view.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.qiitaclient.R
import com.example.qiitaclient.databinding.FragmentArticleListBinding
import com.example.qiitaclient.databinding.ItemArticleListBinding
import com.example.qiitaclient.databinding.ItemBottomBinding
import com.example.qiitaclient.domain.model.ArticleWithTag
import com.example.qiitaclient.presentation.viewmodel.ArticleListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleListFragment : Fragment(R.layout.fragment_article_list) {

    private val viewModel: ArticleListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentArticleListBinding.bind(view)
        val adapter = ArticleListAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.articleList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "エラーが発生しました", Toast.LENGTH_SHORT).show()
                viewModel.onConsumeError()
            }
        }
        viewModel.onScrollTop.observe(viewLifecycleOwner) {
            if (it) {
                binding.recyclerView.scrollToPosition(0)
                viewModel.onScrolledTop()
            }
        }
        viewModel.init()
    }

    class ArticleListAdapter(private val context: Context) :
        ListAdapter<ArticleWithTag, ArticleListAdapter.ViewHolderType>(ArticleDiffCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderType =
            when (viewType) {
                ViewHolderType.ArticleListViewHolder.VIEW_TYPE -> ViewHolderType.ArticleListViewHolder(
                    ItemArticleListBinding.inflate(LayoutInflater.from(context), parent, false)
                )
                ViewHolderType.BottomViewHolder.VIEW_TYPE -> ViewHolderType.BottomViewHolder(
                    ItemBottomBinding.inflate(LayoutInflater.from(context), parent, false)
                )
                else -> throw IllegalStateException("invalid view type")
            }


        override fun onBindViewHolder(holder: ViewHolderType, position: Int) {
            when (holder) {
                is ViewHolderType.ArticleListViewHolder -> {
                    val article = getItem(position).article
                    holder.binding.let {
                        it.article = article
                        it.constrainLayout.setOnClickListener {
                            val uri = Uri.parse(article.url)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(intent)
                        }
                    }
                }
                is ViewHolderType.BottomViewHolder -> {
                    // do nothing
                }
            }

        }

        override fun getItemCount(): Int = if (currentList.isNotEmpty()) currentList.size + 1 else 0

        override fun getItemViewType(position: Int): Int = when (position) {
            currentList.size -> ViewHolderType.BottomViewHolder.VIEW_TYPE
            else -> ViewHolderType.ArticleListViewHolder.VIEW_TYPE
        }

        class ArticleDiffCallback : DiffUtil.ItemCallback<ArticleWithTag>() {
            override fun areItemsTheSame(
                oldItem: ArticleWithTag,
                newItem: ArticleWithTag
            ): Boolean =
                oldItem.article == newItem.article

            override fun areContentsTheSame(
                oldItem: ArticleWithTag,
                newItem: ArticleWithTag
            ): Boolean =
                oldItem.article.id == newItem.article.id

        }

        sealed class ViewHolderType(binding: ViewBinding) :
            RecyclerView.ViewHolder(binding.root) {

            class ArticleListViewHolder(val binding: ItemArticleListBinding) :
                ViewHolderType(binding) {
                companion object {
                    const val VIEW_TYPE: Int = 0
                }
            }

            class BottomViewHolder(binding: ItemBottomBinding) :
                ViewHolderType(binding) {
                companion object {
                    const val VIEW_TYPE: Int = 1
                }
            }
        }
    }
}
