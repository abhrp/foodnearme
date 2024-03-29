package com.abhrp.foodnearme.di.module

import com.abhrp.foodnearme.util.logging.AppLogger
import com.abhrp.foodnearme.util.logging.AppLoggerImpl
import dagger.Binds
import dagger.Module

@Module
abstract class UtilModule {

    @Binds
    abstract fun bindsAppLogger(appLoggerImpl: AppLoggerImpl): AppLogger

}