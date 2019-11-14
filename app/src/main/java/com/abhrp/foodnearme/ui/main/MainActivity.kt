package com.abhrp.foodnearme.ui.main

import android.Manifest
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.core.content.ContextCompat
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
    GoogleMap.OnMyLocationButtonClickListener {

    @Inject
    lateinit var locationMonitor: LocationMonitor

    @Inject
    lateinit var restaurantsViewModel: RestaurantsViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var locationBitmapProvider: LocationBitmapProvider

    private var googleMap: GoogleMap? = null
    private var shouldFetchNewRestaurants = false
    private var isMyLocationClicked = false

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
            val markerOptions = MarkerOptions().position(position).title(title).icon(locationBitmapProvider.foodMarkerBitmap)
            val marker = googleMap?.addMarker(markerOptions)
            marker?.tag = tag
        }
    }

    private fun getBitmapFromDrawable(vectorId: Int): BitmapDescriptor? {
        return  ContextCompat.getDrawable(this, vectorId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
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
        getCurrentLocationWithPermissionCheck()
    }

    override fun onCameraMove() {

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
        if (shouldFetchNewRestaurants) {
            shouldFetchNewRestaurants = false
            isMyLocationClicked = false
            sendNewRequestForCurrentMapBounds()
        }
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
