package com.coinmarker.coinmarker.presentation.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.artventure.artventure.binding.BindingFragment
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.databinding.FragmentMarketBinding
import com.coinmarker.coinmarker.presentation.MainViewModel
import com.coinmarker.coinmarker.presentation.adapter.MarketAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MarketFragment : BindingFragment<FragmentMarketBinding>(R.layout.fragment_market) {
    private val viewModel : MainViewModel by activityViewModels()

    private val adapter by lazy {
        MarketAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter(){
        with(binding){
            rvMarketSearchResult.adapter = adapter.apply {
                submitList(
                    viewModel.assets
                )
            }
        }

    }

}