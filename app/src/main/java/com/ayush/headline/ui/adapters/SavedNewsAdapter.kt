package com.ayush.headline.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.ayush.headline.R
import com.ayush.headline.data.models.Article
import com.ayush.headline.data.models.LikedArticle
import com.ayush.headline.databinding.NewsItemBinding
import com.ayush.headline.utils.NewsItemClicksListener

class SavedNewsAdapter(
    private val list: List<LikedArticle>,
    private val listener: NewsItemClicksListener
) : RecyclerView.Adapter<SavedNewsAdapter.SavedNewsVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedNewsVH {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedNewsVH(binding)
    }

    override fun onBindViewHolder(holder: SavedNewsVH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class SavedNewsVH(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LikedArticle) {
            binding.tvTitle.text = item.title
            binding.ivThumbnail.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.exclamation)
                transformations(RoundedCornersTransformation())
            }
            binding.ivThumbnail.setOnClickListener {
                listener.onItemClicked(likedArticle = item, article = Article())
            }
        }
    }
}