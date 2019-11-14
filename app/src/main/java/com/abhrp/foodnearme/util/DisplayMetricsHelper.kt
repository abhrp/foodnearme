package com.abhrp.foodnearme.util

import android.content.Context
import javax.inject.Inject

class DisplayMetricsHelper @Inject constructor(private val context: Context) {

    /**
     * Gets the pixels for a given dp value
     * @param dp Values in dp
     */
    fun pixelFromDp(dp: Int): Int {
        return (context.resources.displayMetrics.density * dp).toInt()
    }

    fun getDisplayWidth(): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getDisplayHeight(): Int {
        return context.resources.displayMetrics.heightPixels
    }
}