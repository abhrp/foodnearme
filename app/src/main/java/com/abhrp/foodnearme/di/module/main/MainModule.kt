package com.abhrp.foodnearme.di.module.main

import com.abhrp.foodnearme.data.main.RestaurantsRemote
import com.abhrp.foodnearme.data.main.RestaurantsRepositoryImpl
import com.abhrp.foodnearme.domain.repository.RestaurantsRespository
import com.abhrp.foodnearme.remote.impl.main.RestaurantsRemoteImpl
import dagger.Binds
import dagger.Module

@Module
abstract class MainModule {

    @Binds
    abstract fun bindsRepository(restaurantsRepositoryImpl: RestaurantsRepositoryImpl): RestaurantsRespository

    @Binds
    abstract fun bindsRemote(restaurantsRemoteImpl: RestaurantsRemoteImpl): RestaurantsRemote

}