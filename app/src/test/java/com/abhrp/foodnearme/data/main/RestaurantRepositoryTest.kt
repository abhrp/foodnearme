package com.abhrp.foodnearme.data.main

import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.usecase.main.GetRestaurants
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
class RestaurantRepositoryTest {
    private lateinit var restaurantsRepositoryImpl: RestaurantsRepositoryImpl

    @Mock
    private lateinit var restaurantsRemote: RestaurantsRemote

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        restaurantsRepositoryImpl = RestaurantsRepositoryImpl(restaurantsRemote)
    }

    @Test
    fun testGetRestaurantsCompletes() {
        val restaurants = RestaurantFactory.getRestaurants(10)
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        stubRestaurantRemote(northEast, southWest, restaurants)
        val testObserver = restaurantsRepositoryImpl.getRestaurants(northEast, southWest).test()
        testObserver.assertComplete()
    }

    @Test
    fun testGetRestaurantsReturnsData() {
        val restaurants = RestaurantFactory.getRestaurants(10)
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        stubRestaurantRemote(northEast, southWest, restaurants)
        val testObserver = restaurantsRepositoryImpl.getRestaurants(northEast, southWest).test()
        testObserver.assertValue(ResultWrapper(restaurants, 200, null))
    }

    private fun stubRestaurantRemote(northEast: String, southWest: String, restaurants: List<Restaurant>) {
        whenever(restaurantsRemote.getRestaurants(northEast, southWest)).thenReturn(Single.just(
            ResultWrapper(restaurants, 200, null)
        ))
    }
}