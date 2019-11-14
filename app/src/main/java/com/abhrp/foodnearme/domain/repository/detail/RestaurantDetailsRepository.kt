package com.abhrp.foodnearme.domain.repository.detail

import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import io.reactivex.Single

interface RestaurantDetailsRepository {
    /**
     * Gets the details of the required restaurant
     * @param id Identifier of the restaurant to get the details of
     * @return RestaurantDetails
     * @see RestaurantDetails
     */
    fun getRestaurantDetails(id: String): Single<ResultWrapper<RestaurantDetails>>
}