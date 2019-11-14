package com.abhrp.foodnearme.remote.model.detail

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VenueDetails(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "contact") val contact: ContactDetails?,
    @Json(name = "location") val location: LocationDetails?,
    @Json(name = "categories") val categories: List<Category>?,
    @Json(name = "price") val pricing: Pricing?,
    @Json(name = "likes") val likes: Likes?,
    @Json(name = "rating") val rating: Double?,
    @Json(name = "ratingColor") val ratingColor: String?,
    @Json(name = "ratingSignals") val ratingSignals: Int?,
    @Json(name = "menu") val menu: FoodMenu?,
    @Json(name = "timeZone") val timeZone: String?,
    @Json(name = "hours") val hours: Hours?,
    @Json(name = "bestPhoto") val photo: Photo?
)