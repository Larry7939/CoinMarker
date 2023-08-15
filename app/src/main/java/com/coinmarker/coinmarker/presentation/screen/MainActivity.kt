package com.coinmarker.coinmarker.presentation.screen

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.artventure.artventure.binding.BindingActivity
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.databinding.ActivityMainBinding

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }
