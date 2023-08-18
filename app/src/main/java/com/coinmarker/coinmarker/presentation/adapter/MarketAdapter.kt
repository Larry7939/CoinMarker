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
                binding.ibAssetIsArchived.isSelected = isArchivedAsset(data)
                binding.ibAssetIsArchived.setOnClickListener {
                    it.isSelected = !it.isSelected
                    updateArchiveState(data, it.isSelected)
                }
                if(data.changePrice.first() == '-'){
                    binding.tvChangeRate.setTextColor(context.resources.getColor(R.color.blue,null))
                    binding.tvChangePrice.setTextColor(context.resources.getColor(R.color.blue,null))
                }
                else if(data.changePrice.first() == '0'){
                    binding.tvChangeRate.setTextColor(context.resources.getColor(R.color.B1,null))
                    binding.tvChangePrice.setTextColor(context.resources.getColor(R.color.B1,null))
                }
                else{
                    binding.tvChangeRate.setTextColor(context.resources.getColor(R.color.red,null))
                    binding.tvChangePrice.setTextColor(context.resources.getColor(R.color.red,null))
                }
            }
        }
    }
}

