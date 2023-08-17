package com.coinmarker.coinmarker.data.source

import android.content.Context
import androidx.core.content.edit
import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.data.model.AssetEntity
import com.coinmarker.coinmarker.data.util.KorbitLog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
    private val json: Json
) {
    private val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var assets: List<AssetDto>
        set(value) = pref.edit {
            val assetEntity =
                value.map {
                    AssetEntity(
                        currencyPair = it.currencyPair,
                        last = it.last,
                        changePercent = it.changePercent,
                        changePrice = it.changePrice,
                        volume = it.volume,
                        timeStamp = it.timeStamp
                    )
                }
            putString(KEY_ARCHIVE_CONTENTS, json.encodeToString(assetEntity))
        }
        get() {
            val strAssets = pref.getString(KEY_ARCHIVE_CONTENTS, null) ?: return emptyList()
            return try {
                json.decodeFromString<List<AssetEntity>>(strAssets).map { it.toAssetDto() }
            } catch (e: SerializationException) {
                e.message?.let { KorbitLog.e(it) }
                emptyList()
            } catch (e: IllegalArgumentException) {
                e.message?.let { KorbitLog.e(it) }
                emptyList()
            }
        }

    /** 가상자산 정보를 즐겨찾기에 최신 순으로 저장하는 함수, 추가 시 맨 앞(index = 0)에 저장 */
    fun addArchivedAsset(asset: AssetDto) {
        val assets = assets.toMutableList()
        assets.add(0, asset)
        this.assets = assets
    }

    fun removeArchivedAsset(asset: AssetDto) {
        val assets = assets.toMutableList()
        assets.remove(asset)
        this.assets = assets
    }

    /** 가상자산 정보가 즐겨찾기에 저장되어있는지 확인하는 함수 */
    fun isArchivedAsset(asset: AssetDto) = assets.contains(asset)

    companion object {
        private const val FILE_NAME = "korbit_preference"
        private const val KEY_ARCHIVE_CONTENTS = "archivedContents"
    }
}