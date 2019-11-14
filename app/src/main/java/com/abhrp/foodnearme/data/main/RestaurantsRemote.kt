package com.abhrp.foodnearme.data.main

import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import io.reactivex.Single


interface RestaurantsRemote {

    /**
     * Get restaurants from remote source
     * @param northEast The north east bound
     * @param southWest The south west bound
     */
    fun getRestaurants(
        northEast: String,
        southWest: String
    ): Single<ResultWrapper<List<Restaurant>>>
}