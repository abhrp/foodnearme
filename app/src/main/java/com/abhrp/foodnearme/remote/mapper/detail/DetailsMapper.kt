package com.abhrp.foodnearme.remote.mapper.detail

import com.abhrp.foodnearme.domain.model.detail.RestaurantDetails
import com.abhrp.foodnearme.domain.model.detail.RestaurantTiming
import com.abhrp.foodnearme.remote.mapper.RemoteMapper
import com.abhrp.foodnearme.remote.model.detail.Category
import com.abhrp.foodnearme.remote.model.detail.Photo
import com.abhrp.foodnearme.remote.model.detail.TimeFrame
import com.abhrp.foodnearme.remote.model.detail.VenueDetails
import javax.inject.Inject

class DetailsMapper @Inject constructor() :
    RemoteMapper<VenueDetails, RestaurantDetails> {

    override fun mapToData(remoteModel: VenueDetails): RestaurantDetails {
        val id = remoteModel.id
        val name = remoteModel.name
        val imageUrl = getCompleteImageUrl(remoteModel.photo)
        val phone = remoteModel.contact?.phone
        val formattedPhone = remoteModel.contact?.formattedPhone
        val twitter = remoteModel.contact?.twitter
        val facebook = remoteModel.contact?.facebook
        val faceBookName = remoteModel.contact?.facebookName
        val latitude = remoteModel.location?.latitude
        val longitude = remoteModel.location?.longitude
        val address = getFormattedAddress(remoteModel.location?.addressList)
        val categories = getSortedCategoriesString(remoteModel.categories)
        val affordability = remoteModel.pricing?.message
        val noOfLikes = remoteModel.likes?.count
        val rating = remoteModel.rating
        val ratingColor = remoteModel.ratingColor
        val totalRating = remoteModel.ratingSignals
        val menuUrl = remoteModel.menu?.mobileUrl
        val timeZone = remoteModel.timeZone
        val isOpenNow = remoteModel.hours?.isOpen
        val currentStatus = remoteModel.hours?.status
        val timings = getTimings(remoteModel.hours?.timeFrames)
        return RestaurantDetails(
            id, name, imageUrl, phone, formattedPhone, twitter, facebook, faceBookName,
            address, latitude, longitude, categories, affordability, noOfLikes, rating,
            ratingColor, totalRating, menuUrl, timeZone, isOpenNow, currentStatus, timings
        )
    }

    private fun getTimings(timeFrames: List<TimeFrame>?): List<RestaurantTiming>? {
        return timeFrames?.map {
            val times = it.openTime
            val openTime = if (times != null && times.count() > 0) times[0].openTime else null
            RestaurantTiming(it.days, openTime)
        }
    }

    private fun getSortedCategoriesString(categories: List<Category>?): String? {
        return categories?.map { it.name }?.joinToString(", ")
    }

    private fun getFormattedAddress(addresses: List<String>?): String? {
        return addresses?.joinToString(", ")
    }

    private fun getCompleteImageUrl(photo: Photo?): String? {
        if (photo != null) {
            return "${photo.imagePrefix}${photo.imageWidth}x${photo.imageHeight}${photo.imageSuffix}"
        }
        return null
    }
}