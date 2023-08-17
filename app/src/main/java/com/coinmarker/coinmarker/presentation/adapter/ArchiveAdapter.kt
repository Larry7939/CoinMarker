package com.coinmarker.coinmarker.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
            binding.assetData = data
            binding.ibAssetIsArchived.isSelected = isArchivedAsset(data)
            binding.ibAssetIsArchived.setOnClickListener {
                it.isSelected = !it.isSelected
                updateArchiveState(data, it.isSelected)
            }
        }
    }
}