package com.abhrp.foodnearme.data.main

import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.repository.RestaurantsRespository
import io.reactivex.Single
import javax.inject.Inject

class RestaurantsRepositoryImpl @Inject constructor(private val restaurantsRemote: RestaurantsRemote): RestaurantsRespository {
    /**
     * Gets a list of restaurants from a given map bounds
     * @param northEast - The north east or top right bound of the current frame of the map
     * @param southWest - The south west or bottom left bound of the current frame of the map
     * @return A list of Restaurant objects
     * @see Restaurant
     */
    override fun getRestaurants(northEast: String, southWest: String): Single<ResultWrapper<List<Restaurant>>> {
        return restaurantsRemote.getRestaurants(northEast, southWest)
    }
}