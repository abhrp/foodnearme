package com.abhrp.foodnearme.remote.impl.detail

import com.abhrp.foodnearme.data.detail.RestaurantDetailsRemote
import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.remote.config.ErrorUtil
import com.abhrp.foodnearme.remote.mapper.detail.DetailsMapper
import com.abhrp.foodnearme.remote.service.FoodApiFactory
import io.reactivex.Single
import javax.inject.Inject

class RestaurantDetailsRemoteImpl @Inject constructor(
    private val foodApiFactory: FoodApiFactory,
    private val mapper: DetailsMapper,
    private val errorUtil: ErrorUtil
) : RestaurantDetailsRemote {
    /**
     * Gets restaurant details from remote source
     * @param id Id of the restaurant
     */
    override fun getRestaurantDetails(id: String): Single<ResultWrapper<RestaurantDetails>> {
        return foodApiFactory.restaurantsService.getRestaurantDetails(id).map { apiResponse ->
            if (apiResponse.isSuccessful) {
                ResultWrapper<RestaurantDetails>(null, 200, "")
            } else {
                val error = errorUtil.getErrorFromResponse(apiResponse.errorBody())
                ResultWrapper<RestaurantDetails>(null, error?.code, error?.error)
            }
        }
    }
}