package com.abhrp.foodnearme.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.usecase.main.GetRestaurants
import com.abhrp.foodnearme.presentation.state.Resource
import com.abhrp.foodnearme.presentation.state.ResourceState
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class RestaurantsViewModel @Inject constructor(private val getRestaurants: GetRestaurants): ViewModel() {

    override fun onCleared() {
        super.onCleared()
        getRestaurants.disposeAll()
    }

    private val restaurantsLiveData = MutableLiveData<Resource<List<Restaurant>>>()

    fun observerRestaurants(): LiveData<Resource<List<Restaurant>>> = restaurantsLiveData

    fun fetchRestaurants(northEast: String, southWest: String) {
        restaurantsLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        getRestaurants.execute(RestaurantsObserver(), GetRestaurants.Params.getParams(northEast, southWest))
    }

    private inner class RestaurantsObserver: DisposableSingleObserver<List<Restaurant>>() {

        override fun onSuccess(data: List<Restaurant>) {
            restaurantsLiveData.postValue(Resource(ResourceState.SUCCESS, data, null))
        }

        override fun onError(error: Throwable) {
            restaurantsLiveData.postValue(Resource(ResourceState.ERROR, null, error.localizedMessage))
        }
    }
}