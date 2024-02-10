package com.ayush.headline.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayush.headline.R
import com.ayush.headline.data.models.Article
import com.ayush.headline.data.models.NewsItem
import com.ayush.headline.databinding.FragmentHomeBinding
import com.ayush.headline.ui.adapters.NewsAdapter
import com.ayush.headline.ui.viewmodels.HomeViewModel
import com.ayush.headline.utils.Constants
import com.ayush.headline.utils.NetworkUtil
import com.ayush.headline.utils.NewsItemClicksListener
import com.ayush.headline.utils.Response
import com.ayush.headline.utils.SnackbarUtil
import com.ayush.headline.utils.Status
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), NewsItemClicksListener {

    @Inject
    lateinit var networkUtil: NetworkUtil

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private lateinit var context: Context
    private val viewModel: HomeViewModel by viewModels()
    private var category: String = ""
    private var dataByCategory = mutableMapOf<String, NewsItem>()
    private lateinit var newsAdapter: NewsAdapter
    private var networkAvailable: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNav)?.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        viewModel.userOnboarded()
        setupTabs()
        

        networkUtil.observe(viewLifecycleOwner) {
            when (it) {
                Status.AVAILABLE -> {
                    networkAvailable = true
                    viewModel.getHeadlines()
                }

                Status.UNAVAILABLE -> {
                    networkAvailable = false
                    SnackbarUtil(binding.root, Constants.UNAVAILABLE)
                    viewModel.fetchHeadlinesFromDb()
                }

                Status.LOSING -> {
                    networkAvailable = true
                    SnackbarUtil(binding.root, Constants.LOSING)
                }
            }
        }

        viewModel.topHeadlinesState.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Error -> {
                    SnackbarUtil(binding.root, it.message)
                }

                Response.Loading -> {
                    binding.mainLayout.visibility = View.GONE
                    binding.loadingLayout.visibility = View.VISIBLE
                }

                Response.None -> {
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                }

                is Response.Success -> {
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                    if (it.data != null) {
//                        dataByCategory[category] = it.data
                        setHomeData(it.data.articles)
                    }
                }
            }
        }
        
        viewModel.articlesFromDb.observe(viewLifecycleOwner) { it ->
            when(it) {
                is Response.Error -> {
                    Log.d("error", it.message)
                    SnackbarUtil(binding.root, it.message)
                }
                Response.Loading -> {
                    binding.mainLayout.visibility = View.GONE
                    binding.loadingLayout.visibility = View.VISIBLE
                }

                Response.None -> {
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                }
                is Response.Success -> {
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                    Log.d("articles", it.data.toString())
                    setHomeData(it.data)
                }
            }
        }

        viewModel.topHeadlinesByCategoryState.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Error -> {
                    SnackbarUtil(binding.root, it.message)
                }

                Response.Loading -> {
                    binding.mainLayout.visibility = View.GONE
                    binding.loadingLayout.visibility = View.VISIBLE
                }

                Response.None -> {
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                }

                is Response.Success -> {
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                    if (it.data != null) {
                        dataByCategory[category] = it.data
                        setHomeData(dataByCategory[category]?.articles ?: emptyList())
                    }
                }
            }
        }
        return binding.root
    }


    private fun setupTabs() {
        val tabLayout = binding.tabLayout

        Constants.categories.forEach { createTabs(tabLayout, it) }


        tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        category = tab.text.toString().trim().lowercase()
                    }
                    if (networkAvailable) {
                        if (category != "General") {
                            viewModel.getHeadlinesByCategory(category)
                        }
                        if ((dataByCategory[category]?.articles != null)) {
                            setHomeData(dataByCategory[category]!!.articles)
                        }
                    } else {
                        viewModel.fetchHeadlinesFromDb()
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    setHomeData(dataByCategory[category]?.articles ?: emptyList())
                }
            }
        )
    }

    private fun setHomeData(list: List<Article>) {
        newsAdapter = NewsAdapter(this.context, list, this)
        binding.rvNews.adapter = newsAdapter
        binding.rvNews.layoutManager = LinearLayoutManager(this.context)
    }

    private fun createTabs(tabLayout: TabLayout, text: String) {
        tabLayout.addTab(tabLayout.newTab().setText(text))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(url: String, article: Article) {

        val bundle = bundleOf(
            "url" to article.url,
            "imageUrl" to article.urlToImage,
            "title" to article.title,
            "description" to article.description
        )

    }

}