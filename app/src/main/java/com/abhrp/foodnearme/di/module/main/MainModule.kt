package com.abhrp.foodnearme.di.module.main

import androidx.lifecycle.ViewModel
import com.abhrp.foodnearme.data.main.RestaurantsRemote
import com.abhrp.foodnearme.data.main.RestaurantsRepositoryImpl
import com.abhrp.foodnearme.di.annotation.ViewModelKey
import com.abhrp.foodnearme.domain.repository.RestaurantsRespository
import com.abhrp.foodnearme.presentation.viewmodel.RestaurantsViewModel
import com.abhrp.foodnearme.remote.impl.main.RestaurantsRemoteImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(RestaurantsViewModel::class)
    abstract fun bindsFeedViewModel(restaurantsViewModel: RestaurantsViewModel): ViewModel

    @Binds
    abstract fun bindsRepository(restaurantsRepositoryImpl: RestaurantsRepositoryImpl): RestaurantsRespository

    @Binds
    abstract fun bindsRemote(restaurantsRemoteImpl: RestaurantsRemoteImpl): RestaurantsRemote

}