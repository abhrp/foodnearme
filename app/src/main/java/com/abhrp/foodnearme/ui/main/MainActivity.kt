package com.abhrp.foodnearme.ui.main

import android.Manifest
import android.content.res.Resources
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.abhrp.foodnearme.R
import com.abhrp.foodnearme.di.factory.ViewModelFactory
import com.abhrp.foodnearme.presentation.state.ResourceState
import com.abhrp.foodnearme.presentation.viewmodel.RestaurantsViewModel
import com.abhrp.foodnearme.ui.base.BaseActivity
import com.abhrp.foodnearme.util.location.LocationModel
import com.abhrp.foodnearme.util.location.LocationMonitor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.*
import javax.inject.Inject

@RuntimePermissions
class MainActivity : BaseActivity(), OnMapReadyCallback {

    @Inject
    lateinit var locationMonitor: LocationMonitor

    @Inject
    lateinit var restaurantsViewModel: RestaurantsViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        restaurantsViewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantsViewModel::class.java)
        observeForRestaurantsList()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun observeForRestaurantsList() {
        restaurantsViewModel.observerRestaurants().observe(this, Observer { resource ->
            when(resource.state) {
                ResourceState.LOADING -> {}
                ResourceState.SUCCESS -> {}
                ResourceState.ERROR -> {}
            }
        })
    }
    
    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        try {
            googleMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        } catch (e: Resources.NotFoundException) {

        }
        getCurrentLocationWithPermissionCheck()
    }

    private fun observerForLocationChanges() {
        locationMonitor.observe(this, Observer { location ->
            setCurrentLocationOnMap(location)
        })
    }

    private fun setCurrentLocationOnMap(location: LocationModel) {
        val latLng = LatLng(location.latitude, location.longitude)
        googleMap?.isMyLocationEnabled = true
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    override fun online() {
        dismissOfflineSnackBar()
    }

    override fun offline() {
        showOffLineSnackBar(mainContainer)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getCurrentLocation() {
        observerForLocationChanges()
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {
        showRationaleDialog(R.string.permission_location_rationale, request)
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationDenied() {
        showToast(R.string.location_denied)
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onNeverAskLocationPermission() {
        showToast(R.string.never_ask_location)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
