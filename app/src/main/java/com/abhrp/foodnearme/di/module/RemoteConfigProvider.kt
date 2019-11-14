package com.abhrp.foodnearme.di.module

import com.abhrp.foodnearme.BuildConfig
import com.abhrp.foodnearme.remote.config.APIConfigProvider
import com.abhrp.foodnearme.remote.config.BuildTypeProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RemoteConfigProvider {

    @Singleton
    @Provides
    fun apiConfigProvider(): APIConfigProvider = APIConfigProvider(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET)

    @Singleton
    @Provides
    fun buildTypeProvider(): BuildTypeProvider = BuildTypeProvider(BuildConfig.DEBUG)
}