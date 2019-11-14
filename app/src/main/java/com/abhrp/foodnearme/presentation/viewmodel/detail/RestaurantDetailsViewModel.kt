package com.abhrp.foodnearme.presentation.viewmodel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.usecase.detail.GetRestaurantDetails
import com.abhrp.foodnearme.presentation.state.Resource
import com.abhrp.foodnearme.presentation.state.ResourceState
import com.abhrp.foodnearme.util.logging.AppLogger
import io.reactivex.observers.DisposableSingleObserver
import okhttp3.internal.http2.Http2Reader.Companion.logger
import javax.inject.Inject

class RestaurantDetailsViewModel @Inject constructor(private val getRestaurantDetails: GetRestaurantDetails) :
    ViewModel() {

    private val restaurantDetailsLiveData = MutableLiveData<Resource<RestaurantDetails>>()

    override fun onCleared() {
        super.onCleared()
        getRestaurantDetails.disposeAll()
    }

    fun observerRestuarantDetails(): LiveData<Resource<RestaurantDetails>> =
        restaurantDetailsLiveData

    fun fetchRestaurantDetails(id: String) {
        restaurantDetailsLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        getRestaurantDetails.execute(
            RestaurantDetailsObserver(),
            GetRestaurantDetails.Params.getParams(id)
        )
    }


    private inner class RestaurantDetailsObserver :
        DisposableSingleObserver<ResultWrapper<RestaurantDetails>>() {

        override fun onSuccess(result: ResultWrapper<RestaurantDetails>) {
            if (result.code == 200) {
                restaurantDetailsLiveData.postValue(
                    Resource(
                        ResourceState.SUCCESS,
                        result.data,
                        null
                    )
                )
            } else {
                restaurantDetailsLiveData.postValue(
                    Resource(
                        ResourceState.ERROR,
                        null,
                        result.error
                    )
                )
            }
        }

        override fun onError(e: Throwable) {
            restaurantDetailsLiveData.postValue(
                Resource(
                    ResourceState.ERROR,
                    null,
                    e.localizedMessage
                )
            )
        }

    }

}