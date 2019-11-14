package com.abhrp.foodnearme.remote.mapper

interface RemoteMapper<in R, out D> {
    fun mapToData(remoteModel: R): D
}