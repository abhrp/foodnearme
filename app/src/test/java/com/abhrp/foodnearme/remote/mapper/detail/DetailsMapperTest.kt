package com.abhrp.foodnearme.remote.mapper.detail

import com.abhrp.foodnearme.factory.RemoteDataFactory
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DetailsMapperTest {
    private val detailsMapper = DetailsMapper()

    @Test
    fun testDetailsMapperMapsCorrectly() {
        val venueDetails = RemoteDataFactory.getVenueDetails()
        val data = detailsMapper.mapToData(venueDetails)
        Assert.assertEquals(data.id, venueDetails.id)
        Assert.assertEquals(data.name, venueDetails.name)
        Assert.assertEquals(data.affordability, venueDetails.pricing?.message)
        Assert.assertEquals(data.phone, venueDetails.contact?.phone)
        Assert.assertEquals(data.formattedPhone, venueDetails.contact?.formattedPhone)
        Assert.assertEquals(data.facebook, venueDetails.contact?.facebook)
        Assert.assertEquals(data.facebookName, venueDetails.contact?.facebookName)
        Assert.assertEquals(data.twitter, venueDetails.contact?.twitter)
        Assert.assertEquals(data.timeZone, venueDetails.timeZone)
        Assert.assertEquals(data.rating, venueDetails.rating)
        Assert.assertEquals(data.ratingColor, venueDetails.ratingColor)
        Assert.assertEquals(data.totalRatings, venueDetails.ratingSignals)
        Assert.assertEquals(data.menuUrl, venueDetails.menu?.mobileUrl)
        Assert.assertEquals(data.isOpenNow, venueDetails.hours?.isOpen)
        Assert.assertEquals(data.currentStatus, venueDetails.hours?.status)
        Assert.assertEquals(data.longitude, venueDetails.location?.longitude)
        Assert.assertEquals(data.latitude, venueDetails.location?.latitude)
        Assert.assertEquals(data.noOfLikes, venueDetails.likes?.count)
    }
}