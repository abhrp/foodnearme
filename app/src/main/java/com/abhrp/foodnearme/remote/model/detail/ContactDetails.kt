package com.abhrp.foodnearme.remote.model.detail

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContactDetails(
    @Json(name = "phone") val phone: String?,
    @Json(name = "formattedPhone") val formattedPhone: String?,
    @Json(name = "twitter") val twitter: String?,
    @Json(name = "facebook") val facebook: String?,
    @Json(name = "facebookUsername") val facebookUsername: String?
)