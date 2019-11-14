package com.abhrp.foodnearme.remote.impl.main

import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.factory.DataFactory
import com.abhrp.foodnearme.factory.RemoteDataFactory
import com.abhrp.foodnearme.factory.RestaurantFactory
import com.abhrp.foodnearme.remote.config.ErrorUtil
import com.abhrp.foodnearme.remote.mapper.main.VenuesMapper
import com.abhrp.foodnearme.remote.model.main.Venue
import com.abhrp.foodnearme.remote.response.base.FourSquareApiResponse
import com.abhrp.foodnearme.remote.response.base.Meta
import com.abhrp.foodnearme.remote.response.main.RestaurantsResponse
import com.abhrp.foodnearme.remote.service.FoodApiFactory
import com.abhrp.foodnearme.remote.service.RestaurantsService
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class RestaurantsRemoteTest {
    private lateinit var restaurantsRemoteImpl: RestaurantsRemoteImpl

    @Mock
    private lateinit var apiFactory: FoodApiFactory
    @Mock
    private lateinit var restaurantsService: RestaurantsService
    @Mock
    private lateinit var mapper: VenuesMapper
    @Mock
    private lateinit var errorUtil: ErrorUtil

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        restaurantsRemoteImpl = RestaurantsRemoteImpl(apiFactory, mapper, errorUtil)
    }

    @Test
    fun getVenuesCompletes() {
        val venues = RemoteDataFactory.getVenues(10)
        val restaurants = RestaurantFactory.getRestaurants(10)
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        val restaurantsResponse = RestaurantsResponse(venues)
        val fourSquareApiResponse = FourSquareApiResponse(restaurantsResponse, Meta(200, null))
        val response = Response.success(fourSquareApiResponse)
        stubService()
        stubMapper(venues, restaurants)
        stubVenues(northEast, southWest, response)
        val testObserver = restaurantsRemoteImpl.getRestaurants(northEast, southWest).test()
        testObserver.assertComplete()
    }

    @Test
    fun getVenuesReturnsData() {
        val venues = RemoteDataFactory.getVenues(10)
        val restaurants = RestaurantFactory.getRestaurants(10)
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        val restaurantsResponse = RestaurantsResponse(venues)
        val fourSquareApiResponse = FourSquareApiResponse(restaurantsResponse, Meta(200, null))
        val response = Response.success(fourSquareApiResponse)
        stubService()
        stubVenues(northEast, southWest, response)
        stubMapper(venues, restaurants)
        val testObserver = restaurantsRemoteImpl.getRestaurants(northEast, southWest).test()
        testObserver.assertValue(ResultWrapper(restaurants, 200, null))
    }

    private fun stubMapper(venues: List<Venue>, restaurants:List<Restaurant>) {
        for (i in 0 until venues.count()) {
            whenever(mapper.mapToData(venues[i])).thenReturn(restaurants[i])
        }
    }

    private fun stubVenues(northEast: String, southWest: String, response: Response<FourSquareApiResponse<RestaurantsResponse>>) {
        whenever(restaurantsService.getRestaurants(northEast, southWest)).thenReturn(Single.just(response))
    }

    private fun stubService() {
        whenever(apiFactory.restaurantsService).thenReturn(restaurantsService)
    }
}