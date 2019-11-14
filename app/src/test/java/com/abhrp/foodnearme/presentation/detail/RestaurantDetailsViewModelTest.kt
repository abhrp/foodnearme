package com.abhrp.foodnearme.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.usecase.detail.GetRestaurantDetails
import com.abhrp.foodnearme.factory.DataFactory
import com.abhrp.foodnearme.factory.RestaurantFactory
import com.abhrp.foodnearme.presentation.state.ResourceState
import com.abhrp.foodnearme.presentation.viewmodel.detail.RestaurantDetailsViewModel
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
class RestaurantDetailsViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Captor
    private val argumentCaptor = argumentCaptor<DisposableSingleObserver<ResultWrapper<RestaurantDetails>>>()

    private val getRestaurantDetails = mock<GetRestaurantDetails>()
    private val restDetailsViewModel = RestaurantDetailsViewModel(getRestaurantDetails)

    @Test
    fun testGetDetailsCompletes() {
        val id = DataFactory.randomString
        val restaurantDetails = RestaurantFactory.getRestaurantDetails()
        val resultWrapper = ResultWrapper(restaurantDetails, 200, null)
        restDetailsViewModel.fetchRestaurantDetails(id)
        val params = GetRestaurantDetails.Params.getParams(id)
        verify(getRestaurantDetails, Mockito.times(1)).execute(any(), eq(params))
    }

    @Test
    fun testGetDetailsReturnsDataOnSuccess() {
        val id = DataFactory.randomString
        val restaurantDetails = RestaurantFactory.getRestaurantDetails()
        val resultWrapper = ResultWrapper(restaurantDetails, 200, null)
        restDetailsViewModel.fetchRestaurantDetails(id)
        val params = GetRestaurantDetails.Params.getParams(id)
        verify(getRestaurantDetails, Mockito.times(1)).execute(argumentCaptor.capture(), eq(params))
        argumentCaptor.firstValue.onSuccess(resultWrapper)
        Assert.assertEquals(restDetailsViewModel.observeRestaurantDetails().value?.state, ResourceState.SUCCESS)
        Assert.assertEquals(restDetailsViewModel.observeRestaurantDetails().value?.data, restaurantDetails)
        Assert.assertEquals(restDetailsViewModel.observeRestaurantDetails().value?.error, null)
    }

    @Test
    fun testGetDetailsReturnsErrorOnAPIFailure() {
        val id = DataFactory.randomString
        val error = DataFactory.randomString
        val resultWrapper = ResultWrapper<RestaurantDetails>(null, 400, error)
        restDetailsViewModel.fetchRestaurantDetails(id)
        val params = GetRestaurantDetails.Params.getParams(id)
        verify(getRestaurantDetails, Mockito.times(1)).execute(argumentCaptor.capture(), eq(params))
        argumentCaptor.firstValue.onSuccess(resultWrapper)
        Assert.assertEquals(restDetailsViewModel.observeRestaurantDetails().value?.state, ResourceState.ERROR)
        Assert.assertEquals(restDetailsViewModel.observeRestaurantDetails().value?.data, null)
        Assert.assertEquals(restDetailsViewModel.observeRestaurantDetails().value?.error, error)
    }

    @Test
    fun testGetDetailsReturnsErrorOnFailure() {
        val id = DataFactory.randomString
        val error = DataFactory.randomString
        restDetailsViewModel.fetchRestaurantDetails(id)
        val params = GetRestaurantDetails.Params.getParams(id)
        verify(getRestaurantDetails, Mockito.times(1)).execute(argumentCaptor.capture(), eq(params))
        argumentCaptor.firstValue.onError(Throwable(error))
        Assert.assertEquals(restDetailsViewModel.observeRestaurantDetails().value?.state, ResourceState.ERROR)
        Assert.assertEquals(restDetailsViewModel.observeRestaurantDetails().value?.data, null)
        Assert.assertEquals(restDetailsViewModel.observeRestaurantDetails().value?.error, error)
    }
}