package com.abhrp.foodnearme.remote.model.detail

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "prefix") val imagePrefix: String?,
    @Json(name = "suffix") val imageSuffix: String?,
    @Json(name = "width") val imageWidth: Int?,
    @Json(name = "height") val imageHeight: Int?
)