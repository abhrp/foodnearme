package com.abhrp.foodnearme.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Venue(@Json(name = "id") val id: String,
                 @Json(name = "name") val name: String,
                 @Json(name = "location") val location: Location)