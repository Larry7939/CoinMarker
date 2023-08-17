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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private var _getArchivedAssetState = MutableLiveData<UiState>()
    val getArchivedAssetState: LiveData<UiState>
        get() = _getArchivedAssetState

    private var _assets = mutableListOf<AssetDto>()
    val assets:List<AssetDto>
        get() = _assets

    private var _archivedAssets = mutableListOf<AssetDto>()
    val archivedAssets: List<AssetDto>
        get() = _archivedAssets

    fun getAssetInfo() {
        _getAssetInfoState.value = UiState.LOADING
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                withContext(Dispatchers.IO) {
                    marketRepositoryImpl.getAssetInfo()
                }
            }.onSuccess {
                _assets = it
                _getAssetInfoState.value = UiState.SUCCESS
            }.onFailure { throwable ->
                throwable.message?.let { KorbitLog.e(it) }
                _getAssetInfoState.value = UiState.ERROR
            }
        }
    }

    fun getArchivedAsset(){
        _getArchivedAssetState.value = UiState.LOADING
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                withContext(Dispatchers.IO) {
                    _archivedAssets = localStorage.assets.toMutableList()
                }
            }.onSuccess {
                _getArchivedAssetState.value = UiState.SUCCESS
            }.onFailure { throwable ->
                throwable.message?.let { KorbitLog.e(it) }
                _getArchivedAssetState.value = UiState.ERROR
            }
        }
    }

    fun updateArchivedState(asset: AssetDto, isSelected: Boolean) {
        _getArchivedAssetState.value = UiState.LOADING
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                withContext(Dispatchers.IO){
                    if (isSelected) {
                        localStorage.addArchivedAsset(asset)
                    } else {
                        localStorage.removeArchivedAsset(asset)
                    }
                }
            }.onSuccess {
                _archivedAssets = localStorage.assets.toMutableList()
                _getArchivedAssetState.value = UiState.SUCCESS
            }.onFailure { throwable ->
                throwable.message?.let { KorbitLog.e(it) }
                _getArchivedAssetState.value = UiState.ERROR
            }
        }
    }

    fun isArchivedAsset(asset: AssetDto) = localStorage.isArchivedAsset(asset)
}