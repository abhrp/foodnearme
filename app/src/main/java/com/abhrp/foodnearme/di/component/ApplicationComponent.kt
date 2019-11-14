package com.abhrp.foodnearme.di.component

import android.app.Application
import com.abhrp.foodnearme.application.FoodNearMeApp
import com.abhrp.foodnearme.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        RemoteConfigProvider::class,
        PresentationModule::class,
        UtilModule::class,
        UIModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<FoodNearMeApp> {

    override fun inject(instance: FoodNearMeApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}