package com.coinmarker.coinmarker.presentation.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.artventure.artventure.binding.BindingFragment
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.databinding.FragmentArchiveBinding
import com.coinmarker.coinmarker.presentation.MainViewModel
import com.coinmarker.coinmarker.presentation.adapter.ArchiveAdapter
import com.coinmarker.coinmarker.presentation.util.UiState
import com.coinmarker.coinmarker.presentation.util.strategy.AssetSortingStrategy
import com.coinmarker.coinmarker.presentation.util.strategy.VolumeAssetSortingStrategy
import com.coinmarker.coinmarker.presentation.util.type.SortingType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ArchiveFragment : BindingFragment<FragmentArchiveBinding>(R.layout.fragment_archive) {
    private val viewModel: MainViewModel by activityViewModels()
    private val adapter by lazy {
        ArchiveAdapter(
            requireContext(),
            ::isArchivedAsset,
            ::updateArchiveState
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObserver()
        initAdapter()
        initView()

    }

    private fun initView() {
        viewModel.getArchivedAsset()
    }

    private fun addObserver() {
        viewModel.getArchivedAssetState.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiState.LOADING -> {
                    handleLoadingArchive()
                }

                UiState.SUCCESS -> {
                    handleSuccessArchive()
                }

                UiState.EMPTY -> {
                    handleEmptyArchive()
                }

                else -> {}
            }
        }
        viewModel.searchWord.observe(viewLifecycleOwner) { word ->
            if (word.isNotEmpty()) {
                setFilteredAssets(word,
                    viewModel.sortingState.value ?: VolumeAssetSortingStrategy(SortingType.DESCENDING)
                )
            } else {
                binding.tvNoSearchResultWarning.visibility = View.GONE
                adapter.submitList(viewModel.archivedAssets)
            }
        }
        viewModel.sortingState.observe(viewLifecycleOwner) {
            setFilteredAssets(
                viewModel.searchWord.value ?: "",
                viewModel.sortingState.value ?: VolumeAssetSortingStrategy(SortingType.DESCENDING)
            )
        }
    }

    private fun setFilteredAssets(word: String, sorting: AssetSortingStrategy) {
        val filteredAssets = viewModel.archivedAssets.filter {
            it.currencyPair.contains(word)
        }
        if (filteredAssets.isEmpty()) {
            binding.tvEmptyWarning.visibility = View.GONE
            binding.tvNoSearchResultWarning.visibility = View.VISIBLE
        } else {
            binding.tvEmptyWarning.visibility = View.GONE
            binding.tvNoSearchResultWarning.visibility = View.GONE
        }
        adapter.submitList(sorting.sort(filteredAssets))
    }

    private fun handleLoadingArchive() {
        with(binding) {
            pbArchiveLoading.visibility = View.VISIBLE
            tvEmptyWarning.visibility = View.GONE
        }
    }

    private fun handleSuccessArchive() {
        setFilteredAssets(viewModel.searchWord.value ?: "",
            viewModel.sortingState.value ?: VolumeAssetSortingStrategy(SortingType.DESCENDING)
        )
        with(binding) {
            tvEmptyWarning.visibility = View.GONE
            pbArchiveLoading.visibility = View.GONE
            rvArchivedAsset.visibility = View.VISIBLE
        }
    }

    private fun handleEmptyArchive() {
        setFilteredAssets(viewModel.searchWord.value ?: "",
            viewModel.sortingState.value ?: VolumeAssetSortingStrategy(SortingType.DESCENDING)
        )
        with(binding) {
            tvEmptyWarning.visibility = View.VISIBLE
            tvNoSearchResultWarning.visibility = View.GONE
            rvArchivedAsset.visibility = View.GONE
            pbArchiveLoading.visibility = View.GONE
        }
    }

    private fun initAdapter() {
        binding.rvArchivedAsset.adapter = adapter
    }

    private fun isArchivedAsset(asset: AssetDto) = viewModel.isArchivedAsset(asset)

    private fun updateArchiveState(asset: AssetDto, isSelected: Boolean) {
        viewModel.updateArchivedState(asset, isSelected)
    }
}