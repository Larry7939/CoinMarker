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

        /**
        검색어 입력이 없는 경우 전체 목록 출력
        매번 타이핑할 때마다 키워드를 포함하는 가상자산 검색 */
        viewModel.searchWord.observe(viewLifecycleOwner) { word ->
            if (word.isNotEmpty()) {
                setFilteredAssets(word)
            } else {
                binding.tvNoSearchResultWarning.visibility = View.GONE
                adapter.submitList(viewModel.marketAssets)
            }
        }

        viewModel.sortingStrategy.observe(viewLifecycleOwner) {
            setFilteredAssets(
                viewModel.searchWord.value ?: ""

            )
        }

        viewModel.sortingState.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.sortedAssets)
            binding.rvMarketSearchResult.post {
                binding.rvMarketSearchResult.scrollToPosition(0)
            }
        }
    }

    /**
    정렬 및 검색어 키워드 필터링을 수행한 결과를 submitList 후, 최상단 스크롤링 수행 */
    private fun setFilteredAssets(word: String) {
        val filteredAssets = viewModel.marketAssets.filter {
            it.currencyPair.contains(word)
        }
        if (filteredAssets.isEmpty()) {
            binding.tvNoSearchResultWarning.visibility = View.VISIBLE
        } else {
            binding.tvNoSearchResultWarning.visibility = View.GONE
            viewModel.sortFilteredAssets(filteredAssets)
        }
    }

    private fun handleLoadingAssets() {
        with(binding) {
            pbSearchLoading.visibility = View.VISIBLE
        }
    }

    private fun handleSuccessAssets() {
        setFilteredAssets(
            viewModel.searchWord.value ?: ""
        )
        with(binding) {
            pbSearchLoading.visibility = View.GONE
            rvMarketSearchResult.visibility = View.VISIBLE
        }
    }

    private fun handleEmptyAssets() {
        setFilteredAssets(
            viewModel.searchWord.value ?: ""
        )
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

    /** 로컬에 가상자산이 저장되어있는지 여부를 별 모양 버튼에 반영하여 마켓 가상자산 조회 시에 알 수 있도록 하는 메소드 */
    private fun isArchivedAsset(asset: AssetDto) = viewModel.isArchivedAsset(asset)

    /** 즐겨찾기 추가 또는 삭제 */
    private fun updateArchiveState(asset: AssetDto, isSelected: Boolean) {
        viewModel.updateArchivedState(asset, isSelected)
    }
}