package br.ufs.nubankchallenge.core.tests.util

import br.ufs.nubankchallenge.core.domain.notice.ChargebackNotice
import br.ufs.nubankchallenge.core.infrastructure.rest.NubankWebService
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * Created by @ubiratanfsoares
 *
 */

object Fixtures {

    fun chargebackNotice(): ChargebackNotice {
        return ChargebackNotice(
                title = "Antes de continuar",
                rawDescription = "<p>Estamos com você nesta! Certifique-se dos pontos abaixo, são muito importantes:<br/><strong>• Você pode <font color=\\\"#6e2b77\\\">procurar o nome do estabelecimento no Google</font>. Diversas vezes encontramos informações valiosas por lá e elas podem te ajudar neste processo.</strong><br/><strong>• Caso você reconheça a compra, é muito importante pra nós que entre em contato com o estabelecimento e certifique-se que a situação já não foi resolvida.</strong></p>",
                primaryActionText = "Continuar",
                secondaryActionText = "Fechar"
        )
    }

    fun httpError(statusCode: Int, errorMessage: String): HttpException {
        val jsonMediaType = MediaType.parse("application/json")
        val body = ResponseBody.create(jsonMediaType, errorMessage)
        val response: Response<String> = Response.error(statusCode, body)
        return HttpException(response)
    }

    fun nubankWebService(apiURL: String): NubankWebService {

        val httpClient = createHttpClient(logger = createLogger())
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

    private fun createLogger(): Interceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger
    }

    private fun createHttpClient(logger: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
    }
}