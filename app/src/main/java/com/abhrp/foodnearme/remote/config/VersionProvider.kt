package com.abhrp.foodnearme.remote.config

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VersionProvider @Inject constructor() {

    fun getVersionDate(): String {
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return formatter.format(date)
    }
}