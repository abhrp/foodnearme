package com.abhrp.foodnearme.remote.impl.detail

import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.factory.DataFactory
import com.abhrp.foodnearme.factory.RemoteDataFactory
import com.abhrp.foodnearme.factory.RestaurantFactory
import com.abhrp.foodnearme.remote.config.ErrorUtil
import com.abhrp.foodnearme.remote.mapper.detail.DetailsMapper
import com.abhrp.foodnearme.remote.model.detail.VenueDetails
import com.abhrp.foodnearme.remote.response.base.FourSquareApiResponse
import com.abhrp.foodnearme.remote.response.base.Meta
import com.abhrp.foodnearme.remote.response.detail.VenueDetailsResponse
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
class RestaurantDetailsRemoteTest {
    private lateinit var restaurantDetailsRemoteImpl: RestaurantDetailsRemoteImpl

    @Mock
    private lateinit var apiFactory: FoodApiFactory
    @Mock
    private lateinit var mapper: DetailsMapper
    @Mock
    private lateinit var errorUtil: ErrorUtil
    @Mock
    private lateinit var restaurantsService: RestaurantsService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        restaurantDetailsRemoteImpl = RestaurantDetailsRemoteImpl(apiFactory, mapper, errorUtil)
    }

    @Test
    fun getRestaurantDetailsCompletes() {
        val id = DataFactory.randomString
        val venueDetailsResponse = RemoteDataFactory.getResponse()
        val restaurantDetails = RestaurantFactory.getRestaurantDetails()
        val fourSquareApiResponse = FourSquareApiResponse(venueDetailsResponse, Meta(200, null))
        val response = Response.success(fourSquareApiResponse)
        stubService()
        stubDetails(id, response)
        stubMapper(venueDetailsResponse.venueDetails, restaurantDetails)
        val testObserver = restaurantDetailsRemoteImpl.getRestaurantDetails(id).test()
        testObserver.assertComplete()
    }

    @Test
    fun getRestaurantDetailsReturnsData() {
        val id = DataFactory.randomString
        val venueDetailsResponse = RemoteDataFactory.getResponse()
        val restaurantDetails = RestaurantFactory.getRestaurantDetails()
        val fourSquareApiResponse = FourSquareApiResponse(venueDetailsResponse, Meta(200, null))
        val response = Response.success(fourSquareApiResponse)
        stubService()
        stubDetails(id, response)
        stubMapper(venueDetailsResponse.venueDetails, restaurantDetails)
        val testObserver = restaurantDetailsRemoteImpl.getRestaurantDetails(id).test()
        testObserver.assertValue(ResultWrapper(restaurantDetails, 200, null))
    }

    private fun stubMapper(venueDetails: VenueDetails, restaurantDetails: RestaurantDetails) {
        whenever(mapper.mapToData(venueDetails)).thenReturn(restaurantDetails)
    }

    private fun stubDetails(id: String, response: Response<FourSquareApiResponse<VenueDetailsResponse>>) {
        whenever(restaurantsService.getRestaurantDetails(id)).thenReturn(Single.just(response))
    }

    private fun stubService() {
        whenever(apiFactory.restaurantsService).thenReturn(restaurantsService)
    }
}