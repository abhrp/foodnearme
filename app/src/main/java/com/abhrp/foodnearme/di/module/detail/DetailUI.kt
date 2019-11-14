package com.abhrp.foodnearme.di.module.detail

import com.abhrp.foodnearme.di.annotation.ActivityScope
import com.abhrp.foodnearme.ui.detail.RestaurantDetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module(includes = [DetailModule::class])
abstract class DetailUI {
    @get:ContributesAndroidInjector
    @get:ActivityScope
    @get:DetailScope
    abstract val detailsActivity: RestaurantDetailsActivity
}