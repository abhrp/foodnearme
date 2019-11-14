package com.abhrp.foodnearme.domain.model.detail

data class RestaurantDetails(
    val id: String,
    val name: String,
    val imageUrl: String,
    val phone: String?,
    val formattedPhone: String?,
    val twitter: String?,
    val facebook: String?,
    val facebookName: String?,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val categories: String,
    val affordability: String,
    val noOfLikes: Int,
    val rating: Double,
    val ratingColor: String,
    val totalRatings: String,
    val menuUrl: String,
    val timeZone: String,
    val isOpenNow: String,
    val currentStatus: String,
    val timings: List<RestaurantTiming>
)