package com.abhrp.foodnearme.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(@Json(name = "lat") val latitude: Double,
                    @Json(name = "lng") val longitude: Double)