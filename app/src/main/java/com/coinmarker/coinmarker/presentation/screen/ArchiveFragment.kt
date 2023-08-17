package com.coinmarker.coinmarker.presentation.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.artventure.artventure.binding.BindingFragment
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.databinding.FragmentArchiveBinding
import com.coinmarker.coinmarker.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ArchiveFragment : BindingFragment<FragmentArchiveBinding>(R.layout.fragment_archive) {
    private val viewModel :MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}