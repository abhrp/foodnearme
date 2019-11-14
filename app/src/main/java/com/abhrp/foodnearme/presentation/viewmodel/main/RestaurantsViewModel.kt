package com.abhrp.foodnearme.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.usecase.main.GetRestaurants
import com.abhrp.foodnearme.presentation.state.Resource
import com.abhrp.foodnearme.presentation.state.ResourceState
import com.abhrp.foodnearme.util.logging.AppLogger
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class RestaurantsViewModel @Inject constructor(private val getRestaurants: GetRestaurants) :
    ViewModel() {

    override fun onCleared() {
        super.onCleared()
        getRestaurants.disposeAll()
    }

    private val restaurantsLiveData = MutableLiveData<Resource<List<Restaurant>>>()

    fun observerRestaurants(): LiveData<Resource<List<Restaurant>>> = restaurantsLiveData

    fun fetchRestaurants(northEast: String, southWest: String) {
        restaurantsLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        getRestaurants.execute(
            RestaurantsObserver(),
            GetRestaurants.Params.getParams(northEast, southWest)
        )
    }

    private inner class RestaurantsObserver :
        DisposableSingleObserver<ResultWrapper<List<Restaurant>>>() {

        override fun onSuccess(result: ResultWrapper<List<Restaurant>>) {
            if (result.code == 200) {
                val data = result.data
                if (data != null && data.count() > 0) {
                    restaurantsLiveData.postValue(Resource(ResourceState.SUCCESS, data, null))
                } else {
                    restaurantsLiveData.postValue(
                        Resource(
                            ResourceState.ERROR,
                            null,
                            "No restaurants found in the area."
                        )
                    )
                }
            } else {
                restaurantsLiveData.postValue(Resource(ResourceState.ERROR, null, result.error))
            }
        }

        override fun onError(error: Throwable) {
            restaurantsLiveData.postValue(
                Resource(
                    ResourceState.ERROR,
                    null,
                    error.localizedMessage
                )
            )
        }
    }
}