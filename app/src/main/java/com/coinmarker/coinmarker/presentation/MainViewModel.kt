package com.coinmarker.coinmarker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.domain.MarketRepository
import com.coinmarker.coinmarker.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val marketRepositoryImpl: MarketRepository) :
    ViewModel() {
    private var _getAssetInfoState = MutableLiveData<UiState>()
    val getAssetInfoState: LiveData<UiState>
        get() = _getAssetInfoState
    var assets = mutableListOf<AssetDto>()

    fun getAssetInfo() {
        _getAssetInfoState.value = UiState.LOADING
        viewModelScope.launch {
            runCatching {
                marketRepositoryImpl.getAssetInfo()
            }.onSuccess {
                assets = it
                _getAssetInfoState.value = UiState.SUCCESS
            }.onFailure {
                Timber.e(it.message)
                _getAssetInfoState.value = UiState.ERROR
            }
        }
    }
}