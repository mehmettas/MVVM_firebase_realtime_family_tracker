package com.mehmettas.familytrack.ui

import android.R.string
import java.util.*


object IdGenerator {
    private val _base62chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        .toCharArray()

    private val _random = Random()

    fun GetBase62(length: Int): String {
        val sb = StringBuilder(length)

        for (i in 0 until length)
            sb.append(_base62chars[_random.nextInt(62)])

        return sb.toString()
    }

    fun GetBase36(length: Int): String {
        val sb = StringBuilder(length)

        for (i in 0 until length)
            sb.append(_base62chars[_random.nextInt(36)])

        return sb.toString()
    }
}