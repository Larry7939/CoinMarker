package com.coinmarker.coinmarker.data.util

import timber.log.Timber
import java.lang.Math.abs
import java.math.BigDecimal
import java.text.DecimalFormat


fun String.addCommasToPrice(): String {
    val number = this.toIntOrNull()
    if (number != null) {
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(number)
    }
    return this
}
fun String.addCommasToVolume(): String {
    val number = this.toDoubleOrNull()
    if (number != null) {
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(number)
    }
    return this
}
fun String.setPercentFormat(): String {
    val number = this.toDoubleOrNull()
    if (number != null) {
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(number)
    }
    return this
}

fun String.setPriceFormat(): String {
    val number = this.toDoubleOrNull()
    if (number != null) {
        return if (abs(number) >= 100) {
            number.toInt().toString()
        } else {
            val essencePart = BigDecimal(number.toString()).toPlainString().substringBefore('.')
            val decimalPart = BigDecimal(number.toString()).toPlainString().substringAfter('.')
            Timber.d("===== essence $essencePart")
            Timber.d("===== decimal $decimalPart")
            "$essencePart.${decimalPart.getOrElse(0){'0'}}${decimalPart.getOrElse(1){'0'}}"

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