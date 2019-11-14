package com.abhrp.foodnearme.remote.impl.main

import com.abhrp.foodnearme.data.main.RestaurantsRemote
import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.remote.mapper.VenuesMapper
import com.abhrp.foodnearme.remote.service.FoodApiFactory
import io.reactivex.Single
import javax.inject.Inject

class RestaurantsRemoteImpl @Inject constructor(private val foodApiFactory: FoodApiFactory, private val venuesMapper: VenuesMapper): RestaurantsRemote {
    /**
     * Get restaurants from remote source
     * @param northEast The north east bound
     * @param southWest The south west bound
     */
    override fun getRestaurants(northEast: String, southWest: String): Single<List<Restaurant>> {
        return foodApiFactory.restaurantsService.getRestaurants(northEast, southWest).map { apiResponse ->
            apiResponse.response.venues.map {
                venuesMapper.mapToData(it)
            }
        }
    }
}