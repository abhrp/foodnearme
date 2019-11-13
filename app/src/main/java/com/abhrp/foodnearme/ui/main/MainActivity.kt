package com.abhrp.foodnearme.ui.main

import android.Manifest
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.abhrp.foodnearme.R
import com.abhrp.foodnearme.ui.base.BaseActivity
import com.abhrp.foodnearme.util.location.LocationModel
import com.abhrp.foodnearme.util.location.LocationMonitor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.*
import javax.inject.Inject

@RuntimePermissions
class MainActivity : BaseActivity(), OnMapReadyCallback {

    @Inject
    lateinit var locationMonitor: LocationMonitor
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun online() {
        dismissOfflineSnackBar()
    }

    override fun offline() {
        showOffLineSnackBar(mainContainer)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getCurrentLocation() {
        locationMonitor.observe(this, Observer { location ->
            setCurrentLocationOnMap(location)
        })
    }

    private fun setCurrentLocationOnMap(location: LocationModel) {
        val latLng = LatLng(location.latitude, location.longitude)
        googleMap?.isMyLocationEnabled = true
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
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

    private fun showRationaleDialog(@StringRes messageResId: Int, request: PermissionRequest) {
        AlertDialog.Builder(this)
            .setPositiveButton(R.string.button_allow) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.button_deny) { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        getCurrentLocationWithPermissionCheck()
    }
}
