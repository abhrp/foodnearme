package com.abhrp.foodnearme.remote.service

import com.abhrp.foodnearme.remote.config.RemoteConstants
import com.abhrp.foodnearme.remote.response.base.FourSquareApiResponse
import com.abhrp.foodnearme.remote.response.detail.VenueDetailsResponse
import com.abhrp.foodnearme.remote.response.main.RestaurantsResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantsService {

    @GET(RemoteConstants.RESTAURANTS_API)
    fun getRestaurants(@Query("ne") northEast: String, @Query("sw") southWest: String): Single<Response<FourSquareApiResponse<RestaurantsResponse>>>

    @GET(RemoteConstants.RESTAURANTS_DETAILS_API)
    fun getRestaurantDetails(@Path("id") id: String): Single<Response<FourSquareApiResponse<VenueDetailsResponse>>>
}