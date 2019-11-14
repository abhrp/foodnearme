package com.abhrp.foodnearme.remote.model.detail

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Likes(@Json(name = "count") val count: Int?)