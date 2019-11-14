package com.abhrp.foodnearme.domain.model.wrapper

data class ResultWrapper<T>(val data: T?, val code: Int?, val error: String?)