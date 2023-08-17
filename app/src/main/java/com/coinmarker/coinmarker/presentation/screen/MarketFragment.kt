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
import com.coinmarker.coinmarker.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketFragment : BindingFragment<FragmentMarketBinding>(R.layout.fragment_market) {
    private val viewModel: MainViewModel by activityViewModels()

    private val adapter by lazy {
        MarketAdapter(requireContext(), ::isArchivedAsset, ::updateArchiveState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initView()
        addObserver()
    }

    private fun initView() {
        viewModel.getMarketAssets()
    }

    private fun addObserver() {
        viewModel.getAssetInfoState.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiState.LOADING -> handleLoadingAssets()
                UiState.SUCCESS -> handleSuccessAssets()
                UiState.EMPTY -> handleEmptyAssets()
                else -> {}
            }
        }
        viewModel.searchWord.observe(viewLifecycleOwner) { word ->
            if (word.isNotEmpty()) {
                setFilteredAssets(word)
            } else {
                binding.tvNoSearchResultWarning.visibility = View.GONE
                adapter.submitList(viewModel.marketAssets)
            }
        }
    }

    private fun setFilteredAssets(word: String) {
        val filteredAssets = viewModel.marketAssets.filter {
            it.currencyPair.contains(word)
        }
        if (filteredAssets.isEmpty()) {
            binding.tvNoSearchResultWarning.visibility = View.VISIBLE
        } else {
            binding.tvNoSearchResultWarning.visibility = View.GONE
        }
        adapter.submitList(filteredAssets)
    }

    private fun handleLoadingAssets() {
        with(binding) {
            pbSearchLoading.visibility = View.VISIBLE
        }
    }

    private fun handleSuccessAssets() {
        adapter.submitList(viewModel.marketAssets)
        with(binding) {
            pbSearchLoading.visibility = View.GONE
            rvMarketSearchResult.visibility = View.VISIBLE
        }
    }

    private fun handleEmptyAssets() {
        adapter.submitList(viewModel.marketAssets)
        with(binding) {
            pbSearchLoading.visibility = View.GONE
            rvMarketSearchResult.visibility = View.GONE
        }
    }

    private fun initAdapter() {
        with(binding) {
            rvMarketSearchResult.adapter = adapter
        }
    }

    private fun isArchivedAsset(asset: AssetDto) = viewModel.isArchivedAsset(asset)


    private fun updateArchiveState(asset: AssetDto, isSelected: Boolean) {
        viewModel.updateArchivedState(asset, isSelected)
    }
}