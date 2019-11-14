package com.abhrp.foodnearme.remote.model.detail

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    @Json(name = "shortName") val name: String?,
    @Json(name = "primary") val isPrimaryCategory: Boolean?
)