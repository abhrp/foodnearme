package com.abhrp.foodnearme.remote.response.base

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class FourSquareApiError(@Json(name = "meta") val meta: Meta)