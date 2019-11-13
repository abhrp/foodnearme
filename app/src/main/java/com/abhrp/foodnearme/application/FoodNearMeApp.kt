package com.abhrp.foodnearme.application

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class FoodNearMeApp: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
    }
}