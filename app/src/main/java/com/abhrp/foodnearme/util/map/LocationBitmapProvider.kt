package com.abhrp.foodnearme.util.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.abhrp.foodnearme.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import javax.inject.Inject

class LocationBitmapProvider @Inject constructor(private val context: Context) {

    val foodMarkerBitmap: BitmapDescriptor?
        get() {
            return ContextCompat.getDrawable(context, R.drawable.ic_marker_food)?.run {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                val bitmap =
                    Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
                draw(Canvas(bitmap))
                BitmapDescriptorFactory.fromBitmap(bitmap)
            }
        }

}