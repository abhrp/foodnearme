package com.abhrp.foodnearme.di.module.detail

import androidx.lifecycle.ViewModel
import com.abhrp.foodnearme.data.detail.RestaurantDetailsRemote
import com.abhrp.foodnearme.data.detail.RestaurantDetailsRepositoryImpl
import com.abhrp.foodnearme.di.annotation.ViewModelKey
import com.abhrp.foodnearme.domain.repository.detail.RestaurantDetailsRepository
import com.abhrp.foodnearme.presentation.viewmodel.detail.RestaurantDetailsViewModel
import com.abhrp.foodnearme.remote.impl.detail.RestaurantDetailsRemoteImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DetailModule {
    @Binds
    @IntoMap
    @ViewModelKey(RestaurantDetailsViewModel::class)
    abstract fun bindsRestaurantDetailsViewModel(restaurantsViewModel: RestaurantDetailsViewModel): ViewModel

    @Binds
    abstract fun bindsRepository(restaurantDetailsRepositoryImpl: RestaurantDetailsRepositoryImpl): RestaurantDetailsRepository

    @Binds
    abstract fun bindsRemote(restaurantDetailsRemoteImpl: RestaurantDetailsRemoteImpl): RestaurantDetailsRemote
}