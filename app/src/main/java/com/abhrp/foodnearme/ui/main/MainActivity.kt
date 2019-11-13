package com.abhrp.foodnearme.ui.main

import android.os.Bundle
import com.abhrp.foodnearme.R
import com.abhrp.foodnearme.ui.base.BaseActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


class MainActivity : BaseActivity(), OnMapReadyCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun online() {

    }

    override fun offline() {

    }

    override fun onMapReady(map: GoogleMap?) {
    }
}
