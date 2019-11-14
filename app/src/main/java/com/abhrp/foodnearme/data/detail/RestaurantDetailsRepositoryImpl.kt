package com.abhrp.foodnearme.data.detail

import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.repository.detail.RestaurantDetailsRepository
import io.reactivex.Single
import javax.inject.Inject

class RestaurantDetailsRepositoryImpl @Inject constructor(private val restaurantDetailsRemote: RestaurantDetailsRemote) :
    RestaurantDetailsRepository {
    /**
     * Gets the details of the required restaurant
     * @param id Identifier of the restaurant to get the details of
     * @return RestaurantDetails
     * @see RestaurantDetails
     */
    override fun getRestaurantDetails(id: String): Single<ResultWrapper<RestaurantDetails>> {
        return restaurantDetailsRemote.getRestaurantDetails(id)
    }
}