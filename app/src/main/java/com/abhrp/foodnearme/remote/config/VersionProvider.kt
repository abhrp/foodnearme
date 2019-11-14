package com.abhrp.foodnearme.remote.config

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class VersionProvider @Inject constructor() {

    fun getVersionDate(): String {
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return formatter.format(date)
    }
}