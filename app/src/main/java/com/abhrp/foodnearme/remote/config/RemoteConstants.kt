package com.abhrp.foodnearme.remote.config

object RemoteConstants {
    const val TIME_OUT = 60L
    const val CLIENT_ID = "client_id"
    const val CLIENT_SECRET = "client_secret"
    const val VERSION = "v"

    const val BASE_URL = "https://api.foursquare.com/v2/"
    const val RESTAURANTS_API = "venues/search?categoryId=4d4b7105d754a06374d81259&intent=browse"
    const val RESTAURANTS_DETAILS_API = "venues/{id}"
}