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
    private val viewModel :MainViewModel by activityViewModels()
    private val adapter by lazy{ ArchiveAdapter(requireContext(),::isArchivedAsset,::updateArchiveState) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAdapter()
        addObserver()
    }
    private fun initView(){
        viewModel.getArchivedAsset()
    }

    private fun addObserver(){
        viewModel.getArchivedAssetState.observe(viewLifecycleOwner){ state->
            when(state){
                UiState.SUCCESS ->{adapter.submitList(viewModel.archivedAssets)}
                else ->{}
            }
        }
    }
    private fun initAdapter(){
        binding.rvArchivedAsset.adapter = adapter.apply {  }
    }

    private fun isArchivedAsset(asset: AssetDto) = viewModel.isArchivedAsset(asset)

    private fun updateArchiveState(asset: AssetDto, isSelected: Boolean) {
        viewModel.updateArchivedState(asset, isSelected)
    }
}