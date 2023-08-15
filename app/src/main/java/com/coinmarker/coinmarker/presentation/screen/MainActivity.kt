package com.coinmarker.coinmarker.presentation.screen

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.artventure.artventure.binding.BindingActivity
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.databinding.ActivityMainBinding
import com.coinmarker.coinmarker.presentation.util.extension.replace
import com.google.android.material.tabs.TabLayout

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        changeFragment(0)
        addListener()
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