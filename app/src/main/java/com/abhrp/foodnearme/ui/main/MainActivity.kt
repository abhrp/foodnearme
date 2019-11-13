package com.abhrp.foodnearme.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.abhrp.foodnearme.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity(), OnMapReadyCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

//    override fun online() {
//
//    }
//
//    override fun offline() {
//
//    }

    override fun onMapReady(map: GoogleMap?) {
        map?.addMarker(
            MarkerOptions().position(
                LatLng(
                    0.0,
                    0.0
                )
            ).title("Marker")
        )
        Log.i(MainActivity::class.java.canonicalName, "Map ready")
    }
}
