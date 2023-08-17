package com.coinmarker.coinmarker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.data.source.LocalDataSource
import com.coinmarker.coinmarker.data.util.KorbitLog
import com.coinmarker.coinmarker.domain.MarketRepository
import com.coinmarker.coinmarker.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val marketRepositoryImpl: MarketRepository,
    private val localStorage: LocalDataSource
) :
    ViewModel() {
    private var _getAssetInfoState = MutableLiveData<UiState>()
    val getAssetInfoState: LiveData<UiState>
        get() = _getAssetInfoState
    var assets = mutableListOf<AssetDto>()

    private var _archivedAssets = MutableLiveData<MutableList<AssetDto>>()
    val archivedAssets: LiveData<MutableList<AssetDto>>
        get() = _archivedAssets

    fun getAssetInfo() {
        _getAssetInfoState.value = UiState.LOADING
        viewModelScope.launch {
            runCatching {
                marketRepositoryImpl.getAssetInfo()
            }.onSuccess {
                assets = it
                _getAssetInfoState.value = UiState.SUCCESS
            }.onFailure { throwable ->
                throwable.message?.let {
                    KorbitLog.e(it)
                }
                _getAssetInfoState.value = UiState.ERROR
            }
        }
    }

    fun updateArchivedState(asset: AssetDto, isSelected: Boolean) {
        if (isSelected) {
            localStorage.addArchivedAsset(asset)
        } else {
            localStorage.removeArchivedAsset(asset)
        }
        _archivedAssets.value = localStorage.assets.toMutableList()
    }

    fun isArchivedAsset(asset: AssetDto) = localStorage.isArchivedAsset(asset)
}