package com.coinmarker.coinmarker.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.databinding.ItemAssetArchiveBinding
import com.coinmarker.coinmarker.presentation.util.diffutil.AssetItemCallback

class ArchiveAdapter(
    private val context: Context, private val isArchivedAsset: (AssetDto) -> Boolean,
    private val updateArchiveState: (AssetDto, Boolean) -> Unit
) : ListAdapter<AssetDto, ArchiveAdapter.ArchiveViewHolder>(AssetItemCallback()) {
    private val inflater by lazy { LayoutInflater.from(context) }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveViewHolder {
        return ArchiveViewHolder(ItemAssetArchiveBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ArchiveViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    inner class ArchiveViewHolder(private val binding: ItemAssetArchiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: AssetDto) {
            with(binding) {
                assetData = data
                ibAssetIsArchived.isSelected = isArchivedAsset(data)
                ibAssetIsArchived.setOnClickListener {
                    it.isSelected = !it.isSelected
                    updateArchiveState(data, it.isSelected)
                }
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