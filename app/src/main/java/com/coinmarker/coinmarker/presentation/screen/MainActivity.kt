package com.coinmarker.coinmarker.presentation.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.artventure.artventure.binding.BindingActivity
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.databinding.ActivityMainBinding
import com.coinmarker.coinmarker.presentation.MainViewModel
import com.coinmarker.coinmarker.presentation.util.extension.replace
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        addListener()
        initView()
    }

    private fun initView() {
        changeFragment(0)
    }

    private fun addListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeFragment(position = tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun changeFragment(position: Int) {
        when (position) {
            0 -> replace<MarketFragment>(binding.fragmentContainerView.id)
            1 -> replace<ArchiveFragment>(binding.fragmentContainerView.id)
            else -> {}
        }
    }
}