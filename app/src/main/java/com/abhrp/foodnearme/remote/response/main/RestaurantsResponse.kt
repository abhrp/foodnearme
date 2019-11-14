package com.abhrp.foodnearme.remote.response.main

import com.abhrp.foodnearme.remote.model.main.Venue
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantsResponse(@Json(name = "venues") val venues: List<Venue>)