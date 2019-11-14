package com.abhrp.foodnearme.domain.usecase.main

import com.abhrp.foodnearme.domain.executor.PostExecutionThread
import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.repository.main.RestaurantsRepository
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
class GetRestaurantsTest {
    
    private lateinit var getRestaurants: GetRestaurants

    @Mock
    private lateinit var postExecutionThread: PostExecutionThread
    @Mock
    private lateinit var restaurantRepository: RestaurantsRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getRestaurants = GetRestaurants(postExecutionThread, restaurantRepository)
    }

    @Test
    fun testGetRestaurantsCompletes() {
        val restaurants = RestaurantFactory.getRestaurants(10)
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        stubGetRestaurants(northEast, southWest, restaurants)
        val params = GetRestaurants.Params.getParams(northEast, southWest)
        val testObserver = getRestaurants.buildUseCaseObservable(params).test()
        testObserver.assertComplete()
    }

    @Test
    fun testGetRestaurantsReturnsData() {
        val restaurants = RestaurantFactory.getRestaurants(10)
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        stubGetRestaurants(northEast, southWest, restaurants)
        val params = GetRestaurants.Params.getParams(northEast, southWest)
        val testObserver = getRestaurants.buildUseCaseObservable(params).test()
        testObserver.assertValue(ResultWrapper(restaurants, 200, null))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testGetRestaurantsThrowsException() {
        val restaurants = RestaurantFactory.getRestaurants(10)
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        stubGetRestaurants(northEast, southWest, restaurants)
        val testObserver = getRestaurants.buildUseCaseObservable(null).test()
        testObserver.assertComplete()
    }


    private fun stubGetRestaurants(northEast: String, southWest: String,restaurants: List<Restaurant>) {
        whenever(restaurantRepository.getRestaurants(northEast, southWest)).thenReturn(Single.just(
            ResultWrapper(restaurants, 200, null)
        ))
    }
    
}