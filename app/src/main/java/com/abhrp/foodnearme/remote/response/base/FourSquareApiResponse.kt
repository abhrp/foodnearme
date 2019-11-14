package com.abhrp.foodnearme.remote.response.base

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FourSquareApiResponse<T>(
    @Json(name = "response") val response: T,
    @Json(name = "meta") val meta: Meta
)