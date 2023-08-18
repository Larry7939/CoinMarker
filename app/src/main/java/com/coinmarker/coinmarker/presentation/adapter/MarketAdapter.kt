package com.coinmarker.coinmarker.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.databinding.ItemAssetSearchResultBinding
import com.coinmarker.coinmarker.presentation.util.diffutil.AssetItemCallback

class MarketAdapter(
    private val context: Context,
    private val isArchivedAsset: (AssetDto) -> Boolean,
    private val updateArchiveState: (AssetDto, Boolean) -> Unit
) :
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
                ibAssetIsArchived.isSelected = isArchivedAsset(data)
                ibAssetIsArchived.setOnClickListener {
                    it.isSelected = !it.isSelected
                    updateArchiveState(data, it.isSelected)
                }

                /** 변동률, 변동가격 색상 업데이트 */
                if (data.changePrice.first() == '-') {
                    tvChangeRate.setTextColor(context.resources.getColor(R.color.blue, null))
                    tvChangePrice.setTextColor(context.resources.getColor(R.color.blue, null))
                } else if (data.changePrice.first() == '0') {
                    tvChangeRate.setTextColor(context.resources.getColor(R.color.B1, null))
                    tvChangePrice.setTextColor(context.resources.getColor(R.color.B1, null))
                } else {
                    tvChangeRate.setTextColor(context.resources.getColor(R.color.red, null))
                    tvChangePrice.setTextColor(context.resources.getColor(R.color.red, null))
                }
            }
        }
    }
}

