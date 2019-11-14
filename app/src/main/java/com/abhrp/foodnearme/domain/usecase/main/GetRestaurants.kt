package com.abhrp.foodnearme.domain.usecase.main

import com.abhrp.foodnearme.domain.executor.PostExecutionThread
import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.wrapper.ResultWrapper
import com.abhrp.foodnearme.domain.repository.main.RestaurantsRepository
import com.abhrp.foodnearme.domain.usecase.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

/**
 * GetRestaurants - Use case class to get a list of restaurants from the data layer
 * @param postExecutionThread - Execution thread for RxJava
 * @param restaurantsRepository - Repository for data layer
 */
class GetRestaurants @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val restaurantsRepository: RestaurantsRepository
) : SingleUseCase<ResultWrapper<List<Restaurant>>, GetRestaurants.Params>(postExecutionThread) {

    override fun buildUseCaseObservable(params: Params?): Single<ResultWrapper<List<Restaurant>>> {
        requireNotNull(params)
        return restaurantsRepository.getRestaurants(params.northEast, params.southWest)
    }

    /**
     * Class to get params
     * @param northEast The north eastern bound parameter
     * @param southWest The south western bound parameter
     */
    data class Params(val northEast: String, val southWest: String) {
        companion object {
            fun getParams(northEast: String, southWest: String) = Params(northEast, southWest)
        }
    }
}