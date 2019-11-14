package com.abhrp.foodnearme.remote.response.detail

import com.abhrp.foodnearme.remote.model.detail.VenueDetails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VenueDetailsResponse(@Json(name = "venue") val venueDetails: VenueDetails)