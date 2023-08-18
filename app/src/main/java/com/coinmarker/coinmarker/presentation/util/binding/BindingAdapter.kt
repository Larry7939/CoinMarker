package com.coinmarker.coinmarker.presentation.util.binding

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.coinmarker.coinmarker.R
import com.coinmarker.coinmarker.presentation.util.type.SortingType
import timber.log.Timber

object BindingAdapter {
    @BindingAdapter("orderingIcon")
    @JvmStatic
    fun orderingIcon(textView: AppCompatTextView, sortingType: SortingType) {
        when (sortingType) {
            SortingType.ASCENDING -> {
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_ascending_order,
                    0
                )
            }

            SortingType.DESCENDING -> {
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_descending_order,
                    0
                )
            }

            SortingType.DEFAULT -> {
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_default_order,
                    0
                )
            }
        }
    }
}