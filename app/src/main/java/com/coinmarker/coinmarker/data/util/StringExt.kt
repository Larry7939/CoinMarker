package com.coinmarker.coinmarker.data.util

import java.text.DecimalFormat

fun String.addCommasToNumber(): String {
    val number = this.toDoubleOrNull()
    if (number != null) {
        val numberFormat = DecimalFormat("#,###")
        return numberFormat.format(number)
    }
    return this
}
fun String.setPercentFormat(): String {
    val number = this.toDoubleOrNull()
    if (number != null) {
        return String.format("%.2f", number)
    }
    return this
}

fun String.setPriceFormat(): String {
    val number = this.toDoubleOrNull()
    if (number != null) {
        return if (number > 100) {
            String.format("%.0f", number)
        } else {
            String.format("%.2f", number)
        }
    }
    return this
}

fun String.setVolumeFormat(): String {
    val decimalSeparatorIndex = this.indexOf('.')
    return if (decimalSeparatorIndex != -1) {
        this.substring(0, decimalSeparatorIndex)
    } else {
        this
    }
}