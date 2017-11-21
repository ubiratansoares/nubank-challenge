package br.ufs.nubankchallenge.core.infrastructure.rest

import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackActionsPayload
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackNoticePayload
import br.ufs.nubankchallenge.core.infrastructure.models.SubmitChargebackBody
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *
 * Created by @ubiratanfsoares
 *
 */

interface NubankWebService {

    @GET("notice") fun chargebackNotice(): Observable<ChargebackNoticePayload>

    @GET("chargeback") fun chargebackActions(): Observable<ChargebackActionsPayload>

    @POST("chargeback") fun blockCard(): Observable<Unit>

    @POST("chargeback") fun unblockCard(): Observable<Unit>

    @POST("chargeback") fun submitChargeback(@Body body: SubmitChargebackBody): Observable<Unit>

    companion object {
        val BASE_URL = "https://nu-mobile-hiring.herokuapp.com/"
    }

}