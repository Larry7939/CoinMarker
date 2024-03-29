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
                setFilteredAssets(word)
            } else {
                binding.tvNoSearchResultWarning.visibility = View.GONE
                binding.rvArchivedAsset.visibility = View.VISIBLE
                setFilteredAssets(word)
            }
        }
        viewModel.sortingStrategy.observe(viewLifecycleOwner) {
            setFilteredAssets(
                viewModel.searchWord.value ?: ""
            )
        }
        viewModel.sortingState.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.sortedAssets)
            binding.rvArchivedAsset.post {
                binding.rvArchivedAsset.scrollToPosition(0)
            }
        }
    }

    private fun setFilteredAssets(word: String) {
        val filteredAssets = viewModel.archivedAssets.filter {
            it.currencyPair.contains(word)
        }
        if (filteredAssets.isEmpty()) {
            binding.tvEmptyWarning.visibility = View.GONE
            binding.tvNoSearchResultWarning.visibility = View.VISIBLE
            binding.rvArchivedAsset.visibility = View.GONE
        } else {
            binding.tvEmptyWarning.visibility = View.GONE
            binding.tvNoSearchResultWarning.visibility = View.GONE
            binding.rvArchivedAsset.visibility = View.VISIBLE
            viewModel.sortFilteredAssets(filteredAssets)
        }
    }

    private fun handleLoadingArchive() {
        with(binding) {
            pbArchiveLoading.visibility = View.VISIBLE
            tvEmptyWarning.visibility = View.GONE
        }
    }

    private fun handleSuccessArchive() {
        setFilteredAssets(
            viewModel.searchWord.value ?: ""
        )
        with(binding) {
            tvEmptyWarning.visibility = View.GONE
            pbArchiveLoading.visibility = View.GONE
            rvArchivedAsset.visibility = View.VISIBLE
        }
    }

    /** 즐겨찾기에 저장된 가상자산이 없는 경우에 대한 예외처리 */
    private fun handleEmptyArchive() {
        setFilteredAssets(
            viewModel.searchWord.value ?: ""
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

    /** 로컬에 가상자산이 저장되어있는지 여부를 별 모양 버튼에 반영하여 마켓 가상자산 조회 시에 알 수 있도록 하는 메소드 */
    private fun isArchivedAsset(asset: AssetDto) = viewModel.isArchivedAsset(asset)

    /** 즐겨찾기 추가 또는 삭제 */
    private fun updateArchiveState(asset: AssetDto, isSelected: Boolean) {
        viewModel.updateArchivedState(asset, isSelected)
    }
}