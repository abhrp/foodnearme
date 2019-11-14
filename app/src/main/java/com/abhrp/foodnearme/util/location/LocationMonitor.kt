package com.abhrp.foodnearme.util.location

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class LocationMonitor @Inject constructor(context: Context) : LiveData<LocationModel>() {

    private var fusedLocationProvider: FusedLocationProviderClient? =
        LocationServices.getFusedLocationProviderClient(context)


    override fun onActive() {
        super.onActive()
        fusedLocationProvider?.lastLocation?.addOnSuccessListener { location: Location? ->
            location?.also {
                setLocationData(it)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationProvider = null
    }

    private fun setLocationData(location: Location) {
        value = LocationModel(location.latitude, location.longitude)
    }

}