package com.abhrp.foodnearme.remote.config

import com.abhrp.foodnearme.remote.constants.RemoteConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OkHttpProvider @Inject constructor(
    private val apiConfigProvider: APIConfigProvider,
    private val buildTypeProvider: BuildTypeProvider,
    private val versionProvider: VersionProvider
) {

    val okHttpClient: OkHttpClient
        get() = createOkHttpClient(
            makeHttpLoggingInterceptor(buildTypeProvider.isDebug),
            apiAuthenticationInterceptor(
                apiConfigProvider.clientId,
                apiConfigProvider.clientSecret,
                versionProvider.getVersionDate()
            )
        )

    private fun createOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        apiAuthenticationInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiAuthenticationInterceptor)
            .connectTimeout(RemoteConstants.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(RemoteConstants.TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    private fun apiAuthenticationInterceptor(
        clientId: String,
        clientSecret: String,
        version: String
    ): Interceptor {
        return Interceptor { chain ->
            val oldRequest = chain.request()
            val oldUrl = oldRequest.url

            val newUrl = oldUrl.newBuilder()
                .addQueryParameter(RemoteConstants.CLIENT_ID, clientId)
                .addQueryParameter(RemoteConstants.CLIENT_SECRET, clientSecret)
                .addQueryParameter(RemoteConstants.VERSION, version)
                .build()

            val requestBuilder = oldRequest.newBuilder().url(newUrl)
            val newRequest = requestBuilder.build()

            chain.proceed(newRequest)
        }
    }

    private fun makeHttpLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return logging
    }
}