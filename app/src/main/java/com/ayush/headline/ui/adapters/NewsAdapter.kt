package com.ayush.headline.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.ayush.headline.R
import com.ayush.headline.data.models.Article
import com.ayush.headline.databinding.NewsItemBinding
import com.ayush.headline.utils.NewsItemClicksListener

class NewsAdapter(
    private val context: Context,
    private val newsArticles: List<Article>,
    private val listener: NewsItemClicksListener
): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsArticles[position])
    }

    override fun getItemCount() = newsArticles.size

    inner class NewsViewHolder(private val binding: NewsItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Article) {
            binding.tvTitle.text = item.title
            binding.ivThumbnail.load(item.urlToImage) {
                crossfade(true)
                placeholder(R.drawable.exclamation)
                transformations(RoundedCornersTransformation())
            }

            binding.newsCard.setOnClickListener {
                listener.onItemClicked(item.url, item)
            }
        }
    }
}