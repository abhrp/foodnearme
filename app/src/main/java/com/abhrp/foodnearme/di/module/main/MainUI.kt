package com.abhrp.foodnearme.di.module.main

import com.abhrp.foodnearme.di.annotation.ActivityScope
import com.abhrp.foodnearme.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [MainModule::class])
abstract class MainUI {
    @get:ContributesAndroidInjector
    @get:ActivityScope
    @get:MainScope
    abstract val mainActivity: MainActivity
}