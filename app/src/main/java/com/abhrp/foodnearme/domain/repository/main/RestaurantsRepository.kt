package com.abhrp.foodnearme.domain.repository.main

import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import io.reactivex.Single

interface RestaurantsRepository {

    /**
     * Gets a list of restaurants from a given map bounds
     * @param northEast - The north east or top right bound of the current frame of the map
     * @param southWest - The south west or bottom left bound of the current frame of the map
     * @return A list of Restaurant objects
     * @see Restaurant
     */
    fun getRestaurants(
        northEast: String,
        southWest: String
    ): Single<ResultWrapper<List<Restaurant>>>
}