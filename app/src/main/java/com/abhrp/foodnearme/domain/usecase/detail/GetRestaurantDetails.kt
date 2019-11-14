package com.abhrp.foodnearme.domain.usecase.detail

import com.abhrp.foodnearme.domain.executor.PostExecutionThread
import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.repository.detail.RestaurantDetailsRepository
import com.abhrp.foodnearme.domain.usecase.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetRestaurantDetails @Inject constructor(postExecutionThread: PostExecutionThread, private val remoteDetailsRepository: RestaurantDetailsRepository): SingleUseCase<ResultWrapper<RestaurantDetails>, GetRestaurantDetails.Params>(postExecutionThread) {

    override fun buildUseCaseObservable(params: Params?): Single<ResultWrapper<RestaurantDetails>> {
        requireNotNull(params)
        requireNotNull(params.id)
        return remoteDetailsRepository.getRestaurantDetails(params.id)
    }

    data class Params(val id: String) {
        companion object {
            fun getParams(id: String) = Params(id)
        }
    }
}