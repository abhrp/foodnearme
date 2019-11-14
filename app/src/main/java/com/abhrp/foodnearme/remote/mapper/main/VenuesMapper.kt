package com.abhrp.foodnearme.remote.mapper.main

import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.domain.model.main.RestaurantLocation
import com.abhrp.foodnearme.remote.mapper.RemoteMapper
import com.abhrp.foodnearme.remote.model.main.Venue
import javax.inject.Inject

class VenuesMapper @Inject constructor() :
    RemoteMapper<Venue, Restaurant> {

    override fun mapToData(remoteModel: Venue): Restaurant {
        return Restaurant(
            remoteModel.id,
            remoteModel.name,
            RestaurantLocation(remoteModel.location.latitude, remoteModel.location.longitude)
        )
    }

}