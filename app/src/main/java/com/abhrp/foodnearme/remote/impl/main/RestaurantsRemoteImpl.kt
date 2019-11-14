package com.abhrp.foodnearme.remote.impl.main

import com.abhrp.foodnearme.data.main.RestaurantsRemote
import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.remote.config.ErrorUtil
import com.abhrp.foodnearme.remote.mapper.main.VenuesMapper
import com.abhrp.foodnearme.remote.response.base.FourSquareApiResponse
import com.abhrp.foodnearme.remote.response.main.RestaurantsResponse
import com.abhrp.foodnearme.remote.service.FoodApiFactory
import io.reactivex.Single
import javax.inject.Inject

class RestaurantsRemoteImpl @Inject constructor(
    private val foodApiFactory: FoodApiFactory,
    private val venuesMapper: VenuesMapper,
    private val errorUtil: ErrorUtil
) : RestaurantsRemote {

    /**
     * Get restaurants from remote source
     * @param northEast The north east bound
     * @param southWest The south west bound
     */
    override fun getRestaurants(
        northEast: String,
        southWest: String
    ): Single<ResultWrapper<List<Restaurant>>> {
        return foodApiFactory.restaurantsService.getRestaurants(northEast, southWest)
            .map { apiResponse ->
                if (apiResponse.isSuccessful) {
                    val body: FourSquareApiResponse<RestaurantsResponse>? = apiResponse.body()
                    val data = body?.response?.venues?.map {
                        venuesMapper.mapToData(it)
                    }
                    ResultWrapper(data, 200, null)
                } else {
                    val error = errorUtil.getErrorFromResponse(apiResponse.errorBody())
                    ResultWrapper<List<Restaurant>>(null, error?.code, error?.error)
                }
            }
    }
}