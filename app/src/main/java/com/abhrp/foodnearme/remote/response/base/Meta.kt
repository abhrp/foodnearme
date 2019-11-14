package com.abhrp.foodnearme.remote.response.base

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "code") val code: Int,
    @Json(name = "errorDetail") val error: String?
)