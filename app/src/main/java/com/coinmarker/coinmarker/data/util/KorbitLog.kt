package com.coinmarker.coinmarker.data.util

import timber.log.Timber

object KorbitLog {
    private fun buildTag(): String =
        Thread.currentThread().stackTrace[4].let { ste ->
            "${ste.fileName}:${ste.lineNumber}#${ste.methodName}"
        }

    fun d(message: String) = Timber.tag(buildTag()).d(message)

    fun v(message: String) = Timber.tag(buildTag()).v(message)

    fun i(message: String) = Timber.tag(buildTag()).i(message)

    fun w(message: String) = Timber.tag(buildTag()).w(message)

    fun e(message: String) = Timber.tag(buildTag()).e(message)
}
