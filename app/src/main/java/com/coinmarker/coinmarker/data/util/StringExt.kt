package com.coinmarker.coinmarker.data.util

import java.text.DecimalFormat

fun String.addCommasToNumber(): String {
    val number = this.toIntOrNull()
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

fun String.toVolumeInteger(): String {
    val doubleValue = this.toDoubleOrNull()
    if (doubleValue != null) {
        return if (doubleValue.isNaN()) {
            this
        } else {
            doubleValue.toInt().toString()
        }
    }
    return this
}