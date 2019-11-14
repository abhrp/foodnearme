package com.abhrp.foodnearme.remote.mapper.main

import com.abhrp.foodnearme.factory.RemoteDataFactory
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class VenuesMapperTest {
    private val venuesMapper = VenuesMapper()

    @Test
    fun testMapperMapsCorrectly() {
        val venue = RemoteDataFactory.getVenue()
        val data = venuesMapper.mapToData(venue)
        Assert.assertEquals(data.id, venue.id)
        Assert.assertEquals(data.name, venue.name)
        Assert.assertEquals(data.location.latitude, venue.location.latitude, 0.0)
        Assert.assertEquals(data.location.longitude, venue.location.longitude, 0.0)
    }
}