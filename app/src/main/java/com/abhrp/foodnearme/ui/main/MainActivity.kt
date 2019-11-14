package com.abhrp.foodnearme.ui.main

import android.Manifest
import android.content.res.Resources
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.abhrp.foodnearme.R
import com.abhrp.foodnearme.di.factory.ViewModelFactory
import com.abhrp.foodnearme.domain.model.main.Restaurant
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
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.*
import javax.inject.Inject

@RuntimePermissions
class MainActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveCanceledListener {

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
                ResourceState.LOADING -> {

                }
                ResourceState.SUCCESS -> {
                    addNewMarkersOnMap(resource.data)
                }
                ResourceState.ERROR -> {
                    showError(resource.error)
                }
            }
        })
    }

    private fun fetchRestaurants(northEast: String, southWest: String) {
        restaurantsViewModel.fetchRestaurants(northEast, southWest)
    }

    private fun addNewMarkersOnMap(restaurants: List<Restaurant>?) {
        restaurants?.map {
            val title = it.name
            val position = LatLng(it.location.latitude, it.location.longitude)
            val tag = it.id
            val markerOptions = MarkerOptions().position(position).title(title).snippet(title)
            val marker = googleMap?.addMarker(markerOptions)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        try {
            googleMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        } catch (e: Resources.NotFoundException) {

        }
        googleMap?.setOnCameraIdleListener(this)
        getCurrentLocationWithPermissionCheck()
    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraMoveCanceled() {
        
    }

    override fun onCameraIdle() {
        sendNewRequestForCurrentMapBounds()
    }

    private fun sendNewRequestForCurrentMapBounds() {
        val latLngBounds = googleMap?.projection?.visibleRegion?.latLngBounds
        if (latLngBounds != null) {
            googleMap?.clear()
            val northEast = "${latLngBounds.northeast.latitude},${latLngBounds.northeast.longitude}"
            val southWest = "${latLngBounds.southwest.latitude},${latLngBounds.southwest.longitude}"
            fetchRestaurants(northEast, southWest)
        }
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
