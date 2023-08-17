package com.coinmarker.coinmarker.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.databinding.ItemAssetSearchResultBinding
import com.coinmarker.coinmarker.presentation.util.diffutil.AssetItemCallback

class MarketAdapter(private val context: Context) :
    ListAdapter<AssetDto, MarketAdapter.MarketViewHolder>(AssetItemCallback()) {
    private val inflater by lazy { LayoutInflater.from(context) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(ItemAssetSearchResultBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    inner class MarketViewHolder(private val binding: ItemAssetSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: AssetDto) {
            with(binding) {
                assetData = data
            }
        }
    }
}

