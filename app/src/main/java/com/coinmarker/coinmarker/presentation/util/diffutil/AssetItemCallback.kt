package com.coinmarker.coinmarker.presentation.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.coinmarker.coinmarker.data.model.AssetDto

class AssetItemCallback : DiffUtil.ItemCallback<AssetDto>() {
    override fun areItemsTheSame(oldItem: AssetDto, newItem: AssetDto): Boolean =
        oldItem.currencyPair == newItem.currencyPair
    override fun areContentsTheSame(oldItem: AssetDto, newItem: AssetDto): Boolean =
        oldItem == newItem
}