package com.abhrp.foodnearme.factory

import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.detail.RestaurantTiming
import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.main.RestaurantLocation
import com.abhrp.foodnearme.remote.model.detail.LocationDetails

object RestaurantFactory {

    fun getRestaurants(count: Int): List<Restaurant> {
        val list = mutableListOf<Restaurant>()
        for (i in 0..count) {
            list.add(getRestaurant())
        }
        return list
    }

    fun getRestaurant(): Restaurant {
        return Restaurant(DataFactory.randomString, DataFactory.randomString, getRestaurantLocation())
    }

    fun getRestaurantLocation(): RestaurantLocation {
        return RestaurantLocation(DataFactory.randomDouble, DataFactory.randomDouble)
    }

    fun getRestaurantDetails(): RestaurantDetails {
        return RestaurantDetails(DataFactory.randomString, DataFactory.randomString, DataFactory.randomString,
            DataFactory.randomString, DataFactory.randomString, DataFactory.randomString, DataFactory.randomString,
            DataFactory.randomString, DataFactory.randomString, DataFactory.randomDouble, DataFactory.randomDouble, DataFactory.randomString,
            DataFactory.randomString, DataFactory.randomInt, DataFactory.randomDouble, DataFactory.randomString, DataFactory.randomInt, DataFactory.randomString,
            DataFactory.randomString, true, DataFactory.randomString, getTimings(2))
    }

    fun getTimings(count: Int): List<RestaurantTiming> {
        val list = mutableListOf<RestaurantTiming>()
        for (i in 0..count) {
            list.add(getTiming())
        }
        return list
    }

    fun getTiming(): RestaurantTiming {
        return RestaurantTiming(DataFactory.randomString, DataFactory.randomString)
    }
}