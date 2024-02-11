package com.ayush.headline.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayush.headline.R
import com.ayush.headline.data.models.Article
import com.ayush.headline.data.models.LikedArticle
import com.ayush.headline.databinding.FragmentFavouritesBinding
import com.ayush.headline.ui.adapters.SavedNewsAdapter
import com.ayush.headline.ui.viewmodels.FavouritesViewModel
import com.ayush.headline.utils.NewsItemClicksListener
import com.ayush.headline.utils.Response
import com.ayush.headline.utils.SnackbarUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavouritesFragment : Fragment(), NewsItemClicksListener {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: FavouritesViewModel by viewModels()
    private lateinit var adapter: SavedNewsAdapter
    private lateinit var context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.findViewById<BottomNavigationView>(R.id.bottomNav)?.visibility = View.VISIBLE
        binding = FragmentFavouritesBinding.inflate(layoutInflater)

        viewModel.getSavedArticlesState.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Error -> SnackbarUtil(binding.root, it.message)
                Response.Loading -> {}
                Response.None -> {}
                is Response.Success -> {
                    this.adapter = SavedNewsAdapter(it.data, this)
                    binding.rvNews.adapter = this.adapter
                    binding.rvNews.layoutManager = LinearLayoutManager(this.context)
                }
            }
        }

        return binding.root
    }

    override fun onItemClicked(article: Article, likedArticle: LikedArticle) {
        val action = FavouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(
            article,
            likedArticle
        )
        findNavController().navigate(action)
    }
}