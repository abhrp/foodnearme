package com.abhrp.foodnearme.di.module

import com.abhrp.foodnearme.application.UIThread
import com.abhrp.foodnearme.di.module.main.MainUI
import com.abhrp.foodnearme.domain.executor.PostExecutionThread
import dagger.Binds
import dagger.Module

@Module(includes = [MainUI::class])
abstract class UIModule {
    @Binds
    abstract fun bindsPostExecutionThread(uiThread: UIThread): PostExecutionThread
}