package com.ayush.headline.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ayush.headline.R
import com.ayush.headline.data.remote.OnboardingItem
import com.ayush.headline.databinding.FragmentOnboardingBinding
import com.ayush.headline.ui.adapters.OnboardingViewPagerAdapter
import com.ayush.headline.utils.NetworkUtil
import com.ayush.headline.utils.SnackbarUtil
import com.ayush.headline.utils.Status
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var context: Context
    private lateinit var adapter: OnboardingViewPagerAdapter

    @Inject
    lateinit var networkUtil: NetworkUtil


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNav)?.visibility = View.GONE


        networkUtil.observe(viewLifecycleOwner) {
            when(it) {
                Status.AVAILABLE -> {}
                Status.UNAVAILABLE -> {
                    SnackbarUtil(binding.root, "No internet connection! Showing limited content")
                }
                Status.LOSING -> {
                    SnackbarUtil(binding.root, "Poor Connection!")
                }
            }
        }

        val viewPager = binding.viewPager

        adapter = OnboardingViewPagerAdapter(
            this.context,
            listOf(
                OnboardingItem(
                    title = context.getString(R.string.intro_title1),
                    description = context.getString(R.string.intro_desc1)
                ),
                OnboardingItem(
                    title = context.getString(R.string.intro_title2),
                    description = context.getString(R.string.intro_desc2)
                ),
                OnboardingItem(
                    title = context.getString(R.string.intro_title3),
                    description = context.getString(R.string.intro_desc3)
                )
            )
        )

        viewPager.adapter = this.adapter
        viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateProgress(position)
            }
        })
        (viewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        binding.btnNext.setOnClickListener {
            if (viewPager.currentItem + 1 < this.adapter.itemCount) {
                viewPager.currentItem += 1
            } else {
                findNavController().navigate(R.id.action_onboardingFragment_to_homeFragment)
            }
        }
        return binding.root
    }

    private fun updateProgress(position: Int) {
        binding.indicator.setProgress((((position + 1).toFloat()) / 3) * 100, true, 500L)
    }

}