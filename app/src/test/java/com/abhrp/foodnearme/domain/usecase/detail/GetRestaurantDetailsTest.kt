package com.abhrp.foodnearme.domain.usecase.detail

import com.abhrp.foodnearme.domain.executor.PostExecutionThread
import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.repository.detail.RestaurantDetailsRepository
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
class GetRestaurantDetailsTest {
    private lateinit var getRestaurantDetails: GetRestaurantDetails

    @Mock
    private lateinit var postExecutionThread: PostExecutionThread
    @Mock
    private lateinit var restaurantDetailsRepository: RestaurantDetailsRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getRestaurantDetails = GetRestaurantDetails(postExecutionThread, restaurantDetailsRepository)
    }

    @Test
    fun testGetDetailsCompletes() {
        val details = RestaurantFactory.getRestaurantDetails()
        val id = DataFactory.randomString
        stubGetDetails(id, details)
        val params = GetRestaurantDetails.Params.getParams(id)
        val testObserver = getRestaurantDetails.buildUseCaseObservable(params).test()
        testObserver.assertComplete()
    }

    @Test
    fun testGetDetailsReturnsData() {
        val details = RestaurantFactory.getRestaurantDetails()
        val id = DataFactory.randomString
        stubGetDetails(id, details)
        val params = GetRestaurantDetails.Params.getParams(id)
        val testObserver = getRestaurantDetails.buildUseCaseObservable(params).test()
        testObserver.assertValue(ResultWrapper(details, 200, null))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testGetDetailsThrowsException() {
        val details = RestaurantFactory.getRestaurantDetails()
        val id = DataFactory.randomString
        stubGetDetails(id, details)
        val testObserver = getRestaurantDetails.buildUseCaseObservable(null).test()
        testObserver.assertComplete()
    }



    private fun stubGetDetails(id: String, restaurantDetails: RestaurantDetails) {
        whenever(restaurantDetailsRepository.getRestaurantDetails(id)).thenReturn(Single.just(
            ResultWrapper(restaurantDetails, 200, null)
        ))
    }
}