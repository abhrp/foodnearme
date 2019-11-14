package com.abhrp.foodnearme.data.detail

import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.usecase.detail.GetRestaurantDetails
import com.abhrp.foodnearme.factory.DataFactory
import com.abhrp.foodnearme.factory.RestaurantFactory
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class RestaurantDetailRepositoryTest {
    private lateinit var restaurantDetailsRepositoryImpl: RestaurantDetailsRepositoryImpl

    @Mock
    private lateinit var restaurantDetailsRemote: RestaurantDetailsRemote

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        restaurantDetailsRepositoryImpl = RestaurantDetailsRepositoryImpl(restaurantDetailsRemote)
    }

    @Test
    fun testGetDetailsCompletes() {
        val details = RestaurantFactory.getRestaurantDetails()
        val id = DataFactory.randomString
        stubRestaurantDetails(id, details)
        val testObserver = restaurantDetailsRepositoryImpl.getRestaurantDetails(id).test()
        testObserver.assertComplete()
    }

    @Test
    fun testGetDetailsReturnsData() {
        val details = RestaurantFactory.getRestaurantDetails()
        val id = DataFactory.randomString
        stubRestaurantDetails(id, details)
        val testObserver = restaurantDetailsRepositoryImpl.getRestaurantDetails(id).test()
        testObserver.assertValue(ResultWrapper(details, 200, null))
    }

    private fun stubRestaurantDetails(id: String, restaurantDetails: RestaurantDetails ){
        whenever(restaurantDetailsRemote.getRestaurantDetails(id)).thenReturn(Single.just(
            ResultWrapper(restaurantDetails, 200, null)
        ))
    }
}