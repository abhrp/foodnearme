package com.abhrp.foodnearme.remote.mapper.detail

import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.remote.mapper.RemoteMapper
import com.abhrp.foodnearme.remote.model.detail.VenueDetails
import javax.inject.Inject

class DetailsMapper @Inject constructor() :
    RemoteMapper<VenueDetails, RestaurantDetails> {
    override fun mapToData(remoteModel: VenueDetails): RestaurantDetails {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}