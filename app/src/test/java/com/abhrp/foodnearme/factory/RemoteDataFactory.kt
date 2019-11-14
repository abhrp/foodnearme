package com.abhrp.foodnearme.factory

import com.abhrp.foodnearme.remote.model.detail.*
import com.abhrp.foodnearme.remote.model.main.Location
import com.abhrp.foodnearme.remote.model.main.Venue
import com.abhrp.foodnearme.remote.response.detail.VenueDetailsResponse

object RemoteDataFactory {

    fun getResponse(): VenueDetailsResponse {
        return VenueDetailsResponse(getVenueDetails())
    }

    fun getVenueDetails(): VenueDetails {
        return VenueDetails(DataFactory.randomString, DataFactory.randomString,
            getContact(), getLocationDetails(), listOf(getCategory()), getPricing(),
            getLikes(), DataFactory.randomDouble, DataFactory.randomString, DataFactory.randomInt,
            getFoodMenu(), DataFactory.randomString, getHours(), getPhoto())
    }

    fun getPricing(): Pricing {
        return Pricing(DataFactory.randomString)
    }

    fun getPhoto(): Photo {
        return Photo(DataFactory.randomString, DataFactory.randomString, DataFactory.randomInt, DataFactory.randomInt)
    }

    fun getLocationDetails(): LocationDetails {
        return LocationDetails(DataFactory.randomDouble, DataFactory.randomDouble, listOf(DataFactory.randomString, DataFactory.randomString, DataFactory.randomString))
    }

    fun getLikes(): Likes {
        return Likes(DataFactory.randomInt)
    }

    fun getHours(): Hours {
        return Hours(DataFactory.randomString, true, listOf(getTimeFrame(), getTimeFrame()))
    }

    fun getTimeFrame(): TimeFrame {
        return TimeFrame(DataFactory.randomString, true, getOpenTimes())
    }

    fun getOpenTimes(): List<OpenTime> {
        return listOf(getOpenTime(), getOpenTime())
    }

    fun getOpenTime(): OpenTime {
        return OpenTime(DataFactory.randomString)
    }

    fun getFoodMenu(): FoodMenu {
        return FoodMenu(DataFactory.randomString)
    }

    fun getContact(): ContactDetails {
        return ContactDetails(DataFactory.randomString, DataFactory.randomString, DataFactory.randomString, DataFactory.randomString, DataFactory.randomString)
    }

    fun getCategory(): Category {
        return Category(DataFactory.randomString, false)
    }

    fun getVenues(count: Int): List<Venue> {
        val list = mutableListOf<Venue>()
        for (i in 0..count) {
            list.add(getVenue())
        }
        return list
    }

    fun getVenue(): Venue {
        return Venue(DataFactory.randomString, DataFactory.randomString, getLocation())
    }

    fun getLocation():Location {
        return Location(DataFactory.randomDouble, DataFactory.randomDouble)
    }
}