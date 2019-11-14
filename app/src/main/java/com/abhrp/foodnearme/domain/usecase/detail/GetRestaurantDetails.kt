package com.abhrp.foodnearme.domain.usecase.detail

import com.abhrp.foodnearme.domain.executor.PostExecutionThread
import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.repository.detail.RestaurantDetailsRepository
import com.abhrp.foodnearme.domain.usecase.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

/**
 * Use case class for getting details for a restaurant.
 * @param postExecutionThread - Execution thread for RxJava
 * @param restaurantDetailsRepository - Repository for data layer
 */
class GetRestaurantDetails @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val restaurantDetailsRepository: RestaurantDetailsRepository
) : SingleUseCase<ResultWrapper<RestaurantDetails>, GetRestaurantDetails.Params>(postExecutionThread) {

    override fun buildUseCaseObservable(params: Params?): Single<ResultWrapper<RestaurantDetails>> {
        requireNotNull(params)
        return restaurantDetailsRepository.getRestaurantDetails(params.id)
    }

    /**
     * Class to get params
     * @param id Id of the restaurant
     */
    data class Params(val id: String) {
        companion object {
            fun getParams(id: String) = Params(id)
        }
    }
}