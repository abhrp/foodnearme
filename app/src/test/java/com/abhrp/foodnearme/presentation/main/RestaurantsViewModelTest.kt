package com.abhrp.foodnearme.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.usecase.main.GetRestaurants
import com.abhrp.foodnearme.factory.DataFactory
import com.abhrp.foodnearme.factory.RestaurantFactory
import com.abhrp.foodnearme.presentation.state.ResourceState
import com.abhrp.foodnearme.presentation.viewmodel.main.RestaurantsViewModel
import com.nhaarman.mockito_kotlin.*
import io.reactivex.observers.DisposableSingleObserver
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Captor
import org.mockito.Mockito

@RunWith(JUnit4::class)
class RestaurantsViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Captor
    private val argumentCaptor = argumentCaptor<DisposableSingleObserver<ResultWrapper<List<Restaurant>>>>()

    private val getRestaurants = mock<GetRestaurants>()
    private val restaurantsViewModel = RestaurantsViewModel(getRestaurants)

    @Test
    fun testGetRestaurantsCompletes() {
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        restaurantsViewModel.fetchRestaurants(northEast, southWest)
        val params = GetRestaurants.Params.getParams(northEast, southWest)
        verify(getRestaurants, Mockito.times(1)).execute(any(), eq(params))
    }

    @Test
    fun testGetRestaurantsReturnsDataOnSuccess() {
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        val restaurants = RestaurantFactory.getRestaurants(10)
        val resultWrapper = ResultWrapper(restaurants, 200, null)
        restaurantsViewModel.fetchRestaurants(northEast, southWest)
        val params = GetRestaurants.Params.getParams(northEast, southWest)
        verify(getRestaurants, Mockito.times(1)).execute(argumentCaptor.capture(), eq(params))
        argumentCaptor.firstValue.onSuccess(resultWrapper)
        Assert.assertEquals(restaurantsViewModel.observerRestaurants().value?.state, ResourceState.SUCCESS)
        Assert.assertEquals(restaurantsViewModel.observerRestaurants().value?.data, restaurants)
        Assert.assertEquals(restaurantsViewModel.observerRestaurants().value?.error, null)
    }

    @Test
    fun testGetRestaurantsReturnsErrorOnFailure() {
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        val error = DataFactory.randomString
        val resultWrapper = ResultWrapper<List<Restaurant>>(null, 400, error)
        restaurantsViewModel.fetchRestaurants(northEast, southWest)
        val params = GetRestaurants.Params.getParams(northEast, southWest)
        verify(getRestaurants, Mockito.times(1)).execute(argumentCaptor.capture(), eq(params))
        argumentCaptor.firstValue.onSuccess(resultWrapper)
        Assert.assertEquals(restaurantsViewModel.observerRestaurants().value?.state, ResourceState.ERROR)
        Assert.assertEquals(restaurantsViewModel.observerRestaurants().value?.data, null)
        Assert.assertEquals(restaurantsViewModel.observerRestaurants().value?.error, error)
    }

    @Test
    fun testGetRestaurantsReturnsErrorOnFailure2() {
        val northEast = DataFactory.randomString
        val southWest = DataFactory.randomString
        val error = DataFactory.randomString
        restaurantsViewModel.fetchRestaurants(northEast, southWest)
        val params = GetRestaurants.Params.getParams(northEast, southWest)
        verify(getRestaurants, Mockito.times(1)).execute(argumentCaptor.capture(), eq(params))
        argumentCaptor.firstValue.onError(Throwable(error))
        Assert.assertEquals(restaurantsViewModel.observerRestaurants().value?.state, ResourceState.ERROR)
        Assert.assertEquals(restaurantsViewModel.observerRestaurants().value?.data, null)
        Assert.assertEquals(restaurantsViewModel.observerRestaurants().value?.error, error)
    }
}