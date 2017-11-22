package br.ufs.hiring.nubankchallenge.factories

import br.ufs.hiring.nubankchallenge.BuildConfig
import br.ufs.nubankchallenge.core.infrastructure.rest.NubankWebService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * Created by @ubiratanfsoares
 *
 */
object WebServiceFactory {

    val webservice by lazy { create() }

    private fun create(): NubankWebService {

        val apiURL = NubankWebService.BASE_URL
        val debuggable = BuildConfig.DEBUG
        val httpClient = createHttpClient(logger = createLogger(debuggable))
        val converter = GsonConverterFactory.create()
        val rxAdapter = RxJava2CallAdapterFactory.create()

        val retrofit = Retrofit.Builder()
                .baseUrl(apiURL)
                .client(httpClient)
                .addConverterFactory(converter)
                .addCallAdapterFactory(rxAdapter)
                .build()

        return retrofit.create(NubankWebService::class.java)
    }

    private fun createLogger(debuggable: Boolean): Interceptor {

        val logger = HttpLoggingInterceptor()
        logger.level = if (debuggable) Level.BASIC else Level.NONE
        return logger
    }

    private fun createHttpClient(logger: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
    }

}