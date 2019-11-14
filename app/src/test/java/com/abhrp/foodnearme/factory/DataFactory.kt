package com.abhrp.foodnearme.factory

import java.util.*

object DataFactory {
    val randomString: String
        get() = UUID.randomUUID().toString()
    val randomInt: Int
        get() = Math.random().toInt()
    val randomLong: Long
        get() = Math.random().toLong()
    val randomDouble: Double
        get() = Math.random().toDouble()
}