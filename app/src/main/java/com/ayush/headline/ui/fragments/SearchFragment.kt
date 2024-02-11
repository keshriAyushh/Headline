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
import com.ayush.headline.databinding.FragmentSearchBinding
import com.ayush.headline.ui.adapters.NewsAdapter
import com.ayush.headline.ui.viewmodels.SearchViewModel
import com.ayush.headline.utils.Constants
import com.ayush.headline.utils.NetworkUtil
import com.ayush.headline.utils.NewsItemClicksListener
import com.ayush.headline.utils.Response
import com.ayush.headline.utils.SnackbarUtil
import com.ayush.headline.utils.Status
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment(), NewsItemClicksListener {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var context: Context

    @Inject
    lateinit var networkUtil: NetworkUtil

    private var isAvailable: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNav)?.visibility = View.VISIBLE

        networkUtil.observe(viewLifecycleOwner) {
            when (it) {
                Status.AVAILABLE -> {
                    isAvailable = true
                }

                Status.UNAVAILABLE -> {
                    isAvailable = false
                    SnackbarUtil(binding.root, Constants.UNAVAILABLE)
                }

                Status.LOSING -> {
                    isAvailable = false
                    SnackbarUtil(binding.root, Constants.LOSING)
                }
            }
        }
        binding.etlQuery.setEndIconOnClickListener {
            if(isAvailable) {
                viewModel.getNewsByQuery(binding.etQuery.text?.trim().toString())

                viewModel.newsByQueryState.observe(viewLifecycleOwner) {
                    when(it) {
                        is Response.Error -> {
                            SnackbarUtil(binding.root, it.message)
                        }
                        Response.Loading -> {
                            binding.loadingLayout.visibility = View.VISIBLE
                        }
                        Response.None -> {
                            binding.loadingLayout.visibility = View.GONE
                        }
                        is Response.Success -> {
                            binding.loadingLayout.visibility = View.GONE
                            if(it.data != null) {
                                binding.rvNews.adapter = NewsAdapter(this.context, it.data.articles, this)
                                binding.rvNews.layoutManager = LinearLayoutManager(this.context)
                            }
                        }
                    }
                }
            } else {
                SnackbarUtil(
                    binding.root,
                    "Please turn on your internet to search for news articles!"
                )
            }
        }



        return binding.root
    }

    override fun onItemClicked(article: Article, likedArticle: LikedArticle) {
        val action =
            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(article, likedArticle)
        findNavController().navigate(action)
    }
}