package com.abhrp.foodnearme.di.module.detail

import com.abhrp.foodnearme.di.annotation.ActivityScope
import com.abhrp.foodnearme.di.module.main.MainScope
import com.abhrp.foodnearme.ui.detail.RestaurantDetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class DetailUI {
    @get:ContributesAndroidInjector
    @get:ActivityScope
    @get:MainScope
    abstract val detailsActivity: RestaurantDetailsActivity
}