package com.coinmarker.coinmarker.presentation.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.artventure.artventure.binding.BindingActivity
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.databinding.ActivityMainBinding
import com.coinmarker.coinmarker.presentation.MainViewModel
import com.coinmarker.coinmarker.presentation.util.binding.BindingAdapter
import com.coinmarker.coinmarker.presentation.util.extension.replace
import com.coinmarker.coinmarker.presentation.util.strategy.AssetNameAssetSortingStrategy
import com.coinmarker.coinmarker.presentation.util.strategy.AssetSortingStrategy
import com.coinmarker.coinmarker.presentation.util.strategy.Hours24AssetSortingStrategy
import com.coinmarker.coinmarker.presentation.util.strategy.LastAssetSortingStrategy
import com.coinmarker.coinmarker.presentation.util.strategy.VolumeAssetSortingStrategy
import com.coinmarker.coinmarker.presentation.util.type.SortingType
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private var beforeSelectedTv: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        addListener()
        initView()
    }

    private fun initView() {
        changeFragment(0)
        initSortingState(binding.tvSortingByVolume)
    }

    /** 최초 정렬 상태 - 거래대금 내림차순*/
    private fun initSortingState(tv: AppCompatTextView) {
        viewModel.setSortingStrategy(VolumeAssetSortingStrategy(), SortingType.DESCENDING)
        setOrderingIcon(tv, SortingType.DESCENDING)
        beforeSelectedTv = tv
    }

    private fun addListener() {
        with(binding) {
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    changeFragment(position = tab!!.position)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

            tvSortingByAssetName.setOnClickListener {
                setSortingState(
                    it as AppCompatTextView,
                    AssetNameAssetSortingStrategy(SortingType.DESCENDING)
                )
            }
            tvSortingByLast.setOnClickListener {
                setSortingState(
                    it as AppCompatTextView,
                    LastAssetSortingStrategy(SortingType.DESCENDING)
                )
            }
            tvSortingBy24Hours.setOnClickListener {
                setSortingState(
                    it as AppCompatTextView,
                    Hours24AssetSortingStrategy(SortingType.DESCENDING)
                )
            }
            tvSortingByVolume.setOnClickListener {
                setSortingState(
                    it as AppCompatTextView,
                    VolumeAssetSortingStrategy(SortingType.DESCENDING)
                )
            }
        }
    }

    /** 직전에 설정한 정렬 상태의 대체 및 새로운 정렬 상태를 UI에 반영함 */
    private fun setSortingState(tv: AppCompatTextView, assetSortingStrategy: AssetSortingStrategy) {
        if (beforeSelectedTv != null) {
            setOrderingIcon(beforeSelectedTv!!, SortingType.DEFAULT)
        }
        beforeSelectedTv = tv

        if (viewModel.sortingState.value!!.javaClass == assetSortingStrategy.javaClass) {
            when (viewModel.sortingState.value!!.sortingType) {
                SortingType.ASCENDING -> {
                    setOrderingIcon(
                        tv,
                        SortingType.DESCENDING
                    )
                    viewModel.setSortingStrategy(assetSortingStrategy, SortingType.DESCENDING)
                }

                SortingType.DESCENDING -> {
                    setOrderingIcon(
                        tv,
                        SortingType.ASCENDING
                    )
                    viewModel.setSortingStrategy(assetSortingStrategy, SortingType.ASCENDING)
                }

                SortingType.DEFAULT -> {
                    setOrderingIcon(
                        tv,
                        SortingType.DESCENDING

                    )
                    viewModel.setSortingStrategy(assetSortingStrategy, SortingType.DESCENDING)
                }
            }
        } else {
            viewModel.setSortingStrategy(assetSortingStrategy, SortingType.DESCENDING)
            setOrderingIcon(tv, SortingType.DESCENDING)
        }
    }

    private fun setOrderingIcon(tv: AppCompatTextView, sortingType: SortingType) {
        BindingAdapter.setOrderingIcon(
            tv,
            sortingType = sortingType
        )
    }

    private fun changeFragment(position: Int) {
        when (position) {
            0 -> replace<MarketFragment>(binding.fragmentContainerView.id)
            1 -> replace<ArchiveFragment>(binding.fragmentContainerView.id)
            else -> {}
        }
    }
}