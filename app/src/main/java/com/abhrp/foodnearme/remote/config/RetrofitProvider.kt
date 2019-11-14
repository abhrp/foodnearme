package com.abhrp.foodnearme.remote.config

import com.abhrp.foodnearme.remote.constants.RemoteConstants
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitProvider @Inject constructor(okHttpProvider: OkHttpProvider) {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(RemoteConstants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpProvider.okHttpClient)
        .build()
}