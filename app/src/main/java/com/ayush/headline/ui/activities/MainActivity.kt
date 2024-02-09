package com.ayush.headline.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.ayush.headline.R
import com.ayush.headline.databinding.ActivityMainBinding
import com.ayush.headline.utils.NetworkUtil
import com.ayush.headline.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var networkUtil: NetworkUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkUtil
            .observe(this) {
                when(it) {
                    Status.AVAILABLE -> binding.text.text = "Available"
                    Status.UNAVAILABLE -> binding.text.text = "Unavailable"
                    Status.LOSING -> binding.text.text = "Losing"
                }
            }
    }
}