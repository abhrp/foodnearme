package com.abhrp.foodnearme.remote.model.detail

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Hours(
    @Json(name = "status") val status: String?,
    @Json(name = "isOpen") val isOpen: Boolean?,
    @Json(name = "timeframes") val timeFrames: List<TimeFrame>?
)