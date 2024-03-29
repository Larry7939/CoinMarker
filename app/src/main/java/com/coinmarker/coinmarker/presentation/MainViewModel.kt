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
import com.coinmarker.coinmarker.presentation.util.strategy.AssetSortingStrategy
import com.coinmarker.coinmarker.presentation.util.strategy.VolumeAssetSortingStrategy
import com.coinmarker.coinmarker.presentation.util.type.SortingType
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
    private var _getMarketAssetsState = MutableLiveData<UiState>()
    val getMarketAssetsState: LiveData<UiState>
        get() = _getMarketAssetsState

    private var _getArchivedAssetState = MutableLiveData<UiState>()
    val getArchivedAssetState: LiveData<UiState>
        get() = _getArchivedAssetState

    private var _marketAssets = mutableListOf<AssetDto>()
    val marketAssets: List<AssetDto>
        get() = _marketAssets

    private var _archivedAssets = mutableListOf<AssetDto>()
    val archivedAssets: List<AssetDto>
        get() = _archivedAssets

    private var _sortedAssets = mutableListOf<AssetDto>()
    val sortedAssets: List<AssetDto>
        get() = _sortedAssets

    private var _sortingState = MutableLiveData<UiState>()
    val sortingState: LiveData<UiState>
        get() = _sortingState

    val searchWord = MutableLiveData<String>()

    private var _sortingStrategy =
        MutableLiveData<AssetSortingStrategy>(VolumeAssetSortingStrategy(SortingType.DESCENDING))
    val sortingStrategy: LiveData<AssetSortingStrategy>
        get() = _sortingStrategy

    fun getMarketAssets() {
        _getMarketAssetsState.value = UiState.LOADING
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                withContext(Dispatchers.IO) {
                    marketRepositoryImpl.getMarketAssets()
                }
            }.onSuccess {
                _marketAssets = it
                if (_marketAssets.isEmpty()){
                    _getMarketAssetsState.value = UiState.EMPTY
                }
                _getMarketAssetsState.value = UiState.SUCCESS
            }.onFailure { throwable ->
                throwable.message?.let { KorbitLog.e(it) }
                _getMarketAssetsState.value = UiState.ERROR
            }
        }
    }

    fun getArchivedAsset() {
        _getArchivedAssetState.value = UiState.LOADING
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                withContext(Dispatchers.IO) {
                    _archivedAssets = localStorage.assets.toMutableList()
                }
            }.onSuccess {
                if (_archivedAssets.isEmpty()) {
                    _getArchivedAssetState.value = UiState.EMPTY
                } else {
                    _getArchivedAssetState.value = UiState.SUCCESS
                }
            }.onFailure { throwable ->
                throwable.message?.let { KorbitLog.e(it) }
                _getArchivedAssetState.value = UiState.ERROR
            }
        }
    }

    /** 즐겨찾기 추가 또는 삭제 */
    fun updateArchivedState(asset: AssetDto, isSelected: Boolean) {
        _getArchivedAssetState.value = UiState.LOADING
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                withContext(Dispatchers.IO) {
                    if (isSelected) {
                        localStorage.addArchivedAsset(asset)
                    } else {
                        localStorage.removeArchivedAsset(asset)
                    }
                }
            }.onSuccess {
                _archivedAssets = localStorage.assets.toMutableList()
                if (_archivedAssets.isEmpty()) {
                    _getArchivedAssetState.value = UiState.EMPTY
                } else {
                    _getArchivedAssetState.value = UiState.SUCCESS
                }
            }.onFailure { throwable ->
                throwable.message?.let { KorbitLog.e(it) }
                _getArchivedAssetState.value = UiState.ERROR
            }
        }
    }

    fun sortFilteredAssets(filteredAssets:List<AssetDto>){
        _sortingState.value = UiState.LOADING
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                withContext(Dispatchers.Default){
                    sortingStrategy.value?.sort(filteredAssets)
                }
            }.onSuccess { sortedAssets->
                _sortedAssets = sortedAssets as MutableList<AssetDto>
                _sortingState.value = UiState.SUCCESS
            }.onFailure { throwable ->
                throwable.message?.let { KorbitLog.e(it) }
                _sortingState.value = UiState.ERROR
            }
        }
    }

    /** 로컬에 가상자산이 저장되어있는지 여부를 별 모양 버튼에 반영하여 마켓 가상자산 조회 시에 알 수 있도록 하는 메소드 */
    fun isArchivedAsset(asset: AssetDto) = localStorage.isArchivedAsset(asset)

    fun setSortingStrategy(sortingStrategy: AssetSortingStrategy, sortingType: SortingType) {
        sortingStrategy.sortingType = sortingType
        val strategy = sortingStrategy.strategyWithSortingType()
        _sortingStrategy.value = strategy
    }
}