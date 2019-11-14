package com.abhrp.foodnearme.data.detail

import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import io.reactivex.Single

interface RestaurantDetailsRemote {
    /**
     * Gets restaurant details from remote source
     * @param id Id of the restaurant
     */
    fun getRestaurantDetails(id: String): Single<ResultWrapper<RestaurantDetails>>
}