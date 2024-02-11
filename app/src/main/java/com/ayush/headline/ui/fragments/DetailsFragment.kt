package com.ayush.headline.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.ayush.headline.R
import com.ayush.headline.data.models.Article
import com.ayush.headline.data.models.LikedArticle
import com.ayush.headline.data.models.Source
import com.ayush.headline.databinding.FragmentDetailsBinding
import com.ayush.headline.ui.viewmodels.DetailsViewModel
import com.ayush.headline.utils.Constants
import com.ayush.headline.utils.NetworkUtil
import com.ayush.headline.utils.Response
import com.ayush.headline.utils.SnackbarUtil
import com.ayush.headline.utils.Status
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var context: Context
    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private var isLiked: Boolean = false

    @Inject
    lateinit var networkUtil: NetworkUtil
    private var networkAvailable: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNav)?.visibility = View.GONE

        BottomSheetBehavior.from(binding.bottomLayout)
            .apply {
                peekHeight = 150
                this.state = BottomSheetBehavior.STATE_COLLAPSED
                isHideable = true
            }
        //Observing network connectivity
        networkUtil.observe(viewLifecycleOwner) {
            when (it) {
                Status.AVAILABLE -> networkAvailable = true
                Status.UNAVAILABLE -> {
                    networkAvailable = false
                    SnackbarUtil(binding.root, Constants.UNAVAILABLE)
                }

                Status.LOSING -> {
                    networkAvailable = true
                    SnackbarUtil(binding.root, Constants.LOSING)
                }
            }
        }

        viewModel.saveArticleState.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Error -> {
                    SnackbarUtil(binding.root, it.message)
                }

                Response.Loading -> {}
                Response.None -> {}
                is Response.Success -> {
                    SnackbarUtil(binding.root, "Added to favourites!")
                }
            }
        }

        viewModel.deleteArticleState.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Error -> SnackbarUtil(binding.root, it.message)
                Response.Loading -> {}
                Response.None -> {}
                is Response.Success -> SnackbarUtil(binding.root, "Removed from favourites!")
            }
        }
        /*
            If args.NewsArticle is simply an object with empty fields(we can compare any of the fields to know this,
            we know details screen is opened from favourites screen,
            therefore, we need to show the like button and hide the delete button.
         */
        if (args.NewsArticle.title != Article().title) {
            binding.tvTitle.text = args.NewsArticle.title
            binding.tvDescription.text = args.NewsArticle.content
            binding.tvPublished.text = args.NewsArticle.source?.name ?: "Anonymous"
            binding.tvPublishedAt.text = args.NewsArticle.publishedAt
            binding.ivArticle.load(args.NewsArticle.urlToImage) {
                crossfade(true)
                placeholder(R.drawable.exclamation)
                transformations(RoundedCornersTransformation())
            }
            binding.btnLike.visibility = View.VISIBLE
            binding.ivDelete.visibility = View.GONE
        } else {
            binding.btnLike.visibility = View.GONE
            binding.ivDelete.visibility = View.VISIBLE

            binding.tvTitle.text = args.LikedArticle.title
            binding.tvDescription.text = args.LikedArticle.content
            binding.tvPublished.text = args.LikedArticle.source?.name ?: "Anonymous"
            binding.tvPublishedAt.text = args.LikedArticle.publishedAt
            binding.btnLike.visibility = View.GONE
            binding.ivArticle.load(args.LikedArticle.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.exclamation)
                transformations(RoundedCornersTransformation())
            }
        }

        binding.ivDelete.setOnClickListener {
            viewModel.deleteArticle(args.LikedArticle)
            findNavController().popBackStack(R.id.favouritesFragment, false)
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLike.setOnClickListener {
            //Same logic as the one above
            if (args.NewsArticle.title != Article().title) {
                isLiked = !isLiked
                val likedArticle = LikedArticle(
                    title = args.NewsArticle.title ?: "",
                    description = args.NewsArticle.description ?: "",
                    imageUrl = args.NewsArticle.urlToImage ?: "",
                    url = args.NewsArticle.url ?: "",
                    source = args.NewsArticle.source ?: Source(),
                    publishedAt = args.NewsArticle.publishedAt ?: "",
                    isLiked = true,
                    content = args.NewsArticle.content!!
                )
                if (isLiked) {
                    viewModel.saveArticle(likedArticle.copy(isLiked = true))
                    binding.btnLike.setButtonDrawable(R.drawable.heart_red)
                } else {
                    binding.btnLike.setButtonDrawable(R.drawable.heart)
                    viewModel.deleteArticle(likedArticle.copy(isLiked = false))
                }
            }
        }

        binding.tvRedirect.setOnClickListener {
            if (networkAvailable) {
                val url =
                    if (args.NewsArticle.title == Article().title) args.LikedArticle.url else args.NewsArticle.url
                val action =
                    DetailsFragmentDirections.actionDetailsFragmentToWebFragment(url!!)
                findNavController().navigate(action)
            } else {
                SnackbarUtil(binding.root, "Please turn on your internet to read the full article!")
            }
        }

        return binding.root
    }
}