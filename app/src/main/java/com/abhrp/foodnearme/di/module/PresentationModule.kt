package com.abhrp.foodnearme.di.module

import androidx.lifecycle.ViewModelProvider
import com.abhrp.foodnearme.di.factory.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class PresentationModule {
    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}