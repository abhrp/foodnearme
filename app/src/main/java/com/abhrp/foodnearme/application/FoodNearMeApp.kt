package com.abhrp.foodnearme.application

import com.abhrp.foodnearme.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * Main application file, extends DaggerApplication
 * @see DaggerApplication
 */
class FoodNearMeApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }

}