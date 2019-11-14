package com.abhrp.foodnearme.remote.response

import com.abhrp.foodnearme.remote.model.Venue
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantsResponse(@Json(name = "venues") val venues: List<Venue>)