package com.coinmarker.coinmarker.presentation.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.artventure.artventure.binding.BindingFragment
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.databinding.FragmentMarketBinding
import com.coinmarker.coinmarker.presentation.MainViewModel
import com.coinmarker.coinmarker.presentation.adapter.MarketAdapter
import com.coinmarker.coinmarker.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketFragment : BindingFragment<FragmentMarketBinding>(R.layout.fragment_market) {
    private val viewModel: MainViewModel by activityViewModels()

    private val adapter by lazy {
        MarketAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initView()
        addObserver()
    }

    private fun initView() {
        viewModel.getAssetInfo()
    }

    private fun addObserver() {
        viewModel.getAssetInfoState.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiState.SUCCESS -> adapter.submitList(viewModel.assets)
                else -> {}
            }
        }
    }

    private fun initAdapter() {
        with(binding) {
            rvMarketSearchResult.adapter = adapter
        }
    }
}