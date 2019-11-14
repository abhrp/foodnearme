package com.abhrp.foodnearme.presentation.state

data class Resource<out T>(val state: ResourceState, val data: T?, val error: String?)