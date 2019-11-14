package com.abhrp.foodnearme.remote.model.detail

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimeFrame(
    @Json(name = "days") val days: String?,
    @Json(name = "includesToday") val includesToday: Boolean?,
    @Json(name = "open") val openTime: List<OpenTime>?
)