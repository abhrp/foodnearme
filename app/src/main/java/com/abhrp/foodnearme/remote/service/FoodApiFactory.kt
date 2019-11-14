package com.abhrp.foodnearme.remote.service

import com.abhrp.foodnearme.remote.config.RetrofitProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodApiFactory @Inject constructor(private val retrofitProvider: RetrofitProvider)  {
    val restaurantsService = retrofitProvider.retrofit.create(RestaurantsService::class.java)
}