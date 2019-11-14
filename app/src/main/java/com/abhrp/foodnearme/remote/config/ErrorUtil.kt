package com.abhrp.foodnearme.remote.config

import com.abhrp.foodnearme.remote.response.base.FourSquareApiError
import com.abhrp.foodnearme.remote.response.base.Meta
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorUtil @Inject constructor() {
    private val moshi = Moshi.Builder().build()

    fun getErrorFromResponse(errorBody: ResponseBody?): Meta? {
        val errorString = errorBody?.string()
        val jsonAdapter = moshi.adapter<FourSquareApiError>(FourSquareApiError::class.java)
        if (errorString != null) {
            val apiError = jsonAdapter.fromJson(errorString)
            return apiError?.meta
        }
        return Meta(400, "Unknown error")
    }
}