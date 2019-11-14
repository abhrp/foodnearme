package com.abhrp.foodnearme.ui.main

import android.Manifest
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.abhrp.foodnearme.R
import com.abhrp.foodnearme.di.factory.ViewModelFactory
import com.abhrp.foodnearme.domain.model.main.Restaurant
import com.abhrp.foodnearme.presentation.state.ResourceState
import com.abhrp.foodnearme.presentation.viewmodel.RestaurantsViewModel
import com.abhrp.foodnearme.ui.base.BaseActivity
import com.abhrp.foodnearme.ui.detail.RestaurantDetailsActivity
import com.abhrp.foodnearme.util.location.LocationModel
import com.abhrp.foodnearme.util.location.LocationMonitor
import com.abhrp.foodnearme.util.logging.AppLogger
import com.abhrp.foodnearme.util.map.LocationBitmapProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.*
import javax.inject.Inject

@RuntimePermissions
class MainActivity : BaseActivity(),
    OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraMoveCanceledListener,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnInfoWindowClickListener {

    @Inject
    lateinit var locationMonitor: LocationMonitor

    @Inject
    lateinit var restaurantsViewModel: RestaurantsViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var locationBitmapProvider: LocationBitmapProvider
    @Inject
    lateinit var logger: AppLogger

    private var googleMap: GoogleMap? = null
    private var shouldFetchNewRestaurants = false
    private var isMyLocationClicked = false
    private var locationPermissionProvided = false

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
                    progressbar.visibility = View.VISIBLE
                }
                ResourceState.SUCCESS -> {
                    progressbar.visibility = View.GONE
                    addNewMarkersOnMap(resource.data)
                }
                ResourceState.ERROR -> {
                    progressbar.visibility = View.GONE
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
            val markerOptions = MarkerOptions().position(position).title(title).icon(locationBitmapProvider.foodMarkerBitmap)
            val marker = googleMap?.addMarker(markerOptions)
            marker?.tag = tag
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        try {
            googleMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        } catch (e: Resources.NotFoundException) {

        }
        googleMap?.setOnCameraIdleListener(this)
        googleMap?.setOnCameraMoveStartedListener(this)
        googleMap?.setOnCameraMoveListener(this)
        googleMap?.setOnCameraMoveCanceledListener(this)
        googleMap?.setOnMyLocationButtonClickListener(this)
        googleMap?.setOnInfoWindowClickListener(this)
        getCurrentLocationWithPermissionCheck()
    }

    override fun onInfoWindowClick(marker: Marker?) {
        if(marker != null) {
            goToDetailsScreen(marker.tag as String)
        }
    }

    private fun goToDetailsScreen(id: String) {
        startActivity(RestaurantDetailsActivity.newInstance(this, id))
    }

    override fun onCameraMove() {
        logger.logDebug("Camera moved")
    }

    override fun onCameraMoveStarted(reason: Int) {
        when(reason) {
            GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE -> {
                shouldFetchNewRestaurants = true
            }
            GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION -> {
                shouldFetchNewRestaurants = false || isMyLocationClicked
            }
            GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION -> {
                shouldFetchNewRestaurants = true
            }
        }
    }

    override fun onCameraMoveCanceled() {
        shouldFetchNewRestaurants = false
    }

    override fun onMyLocationButtonClick(): Boolean {
        isMyLocationClicked = true
        return false
    }

    override fun onCameraIdle() {
        fetchNewRestaurants()
    }


    private fun sendNewRequestForCurrentMapBounds() {
        if (isOnline) {
            val latLngBounds = googleMap?.projection?.visibleRegion?.latLngBounds
            if (latLngBounds != null) {
                googleMap?.clear()
                val northEast = "${latLngBounds.northeast.latitude},${latLngBounds.northeast.longitude}"
                val southWest = "${latLngBounds.southwest.latitude},${latLngBounds.southwest.longitude}"
                fetchRestaurants(northEast, southWest)
            }
        } else {
            showError(getString(R.string.offline_new_results))
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

    private fun setDefaultLocationOnMapIfPermissionDenied() {
        val latLng = LatLng(52.2944975,4.9544256)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    override fun online() {
        dismissOfflineSnackBar()
        fetchNewRestaurants()
    }

    private fun fetchNewRestaurants() {
        if (shouldFetchNewRestaurants) {
            shouldFetchNewRestaurants = false
            isMyLocationClicked = false
            sendNewRequestForCurrentMapBounds()
        }
    }

    override fun offline() {
        showOffLineSnackBar(mainContainer)
        shouldFetchNewRestaurants = true
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getCurrentLocation() {
        locationPermissionProvided = true
        observerForLocationChanges()
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {
        showRationaleDialog(R.string.permission_location_rationale, request)
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationDenied() {
        showToast(R.string.location_denied)
        setDefaultLocationOnMapIfPermissionDenied()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onNeverAskLocationPermission() {
        showToast(R.string.never_ask_location)
        setDefaultLocationOnMapIfPermissionDenied()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
