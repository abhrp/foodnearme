package com.abhrp.foodnearme.remote.model.detail

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDetails(
    @Json(name = "lat") val latitude: Double,
    @Json(name = "lng") val longitude: Double,
    @Json(name = "formattedAddress") val addressList: List<String>?
)