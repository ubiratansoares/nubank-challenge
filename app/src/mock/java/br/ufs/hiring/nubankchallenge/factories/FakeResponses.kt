package br.ufs.hiring.nubankchallenge.factories

import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackActionsPayload
import br.ufs.nubankchallenge.core.infrastructure.models.ChargebackNoticePayload
import br.ufs.nubankchallenge.core.infrastructure.models.OperationResultPayload
import br.ufs.nubankchallenge.core.infrastructure.util.fromJson
import com.google.gson.Gson
import io.reactivex.Observable

/**
 *
 * Created by @ubiratanfsoares
 *
 */

object FakeResponses {

    private val gson by lazy { Gson() }

    fun noticeRetrived(): Observable<ChargebackNoticePayload> {
        val payload = gson.fromJson(fakeNoticeResponse(), ChargebackNoticePayload::class)
        return Observable.just(payload)
    }

    fun chargebackActions(): Observable<ChargebackActionsPayload> {
        val payload = gson.fromJson(fakeChargebackResponse(), ChargebackActionsPayload::class)
        return Observable.just(payload)
    }

    fun chargebackActionsWithPreventiveBlocking(): Observable<ChargebackActionsPayload> {
        val payload = gson.fromJson(
                fakeChargebackWithPreventiveBlocking(),
                ChargebackActionsPayload::class
        )
        return Observable.just(payload)
    }

    fun operationSuccess(): Observable<OperationResultPayload> {
        val payload = gson.fromJson(
                "{\"status\": \"Ok\"}",
                OperationResultPayload::class
        )
        return Observable.just(payload)
    }

    private fun fakeNoticeResponse(): String {
        return "{\n" +
                "    \"title\": \"Antes de continuar\",\n" +
                "    \"description\": \"<p>Estamos com você nesta! Certifique-se dos pontos abaixo, são muito importantes:<br/><strong>• Você pode <font color=\\\"#6e2b77\\\">procurar o nome do estabelecimento no Google</font>. Diversas vezes encontramos informações valiosas por lá e elas podem te ajudar neste processo.</strong><br/><strong>• Caso você reconheça a compra, é muito importante pra nós que entre em contato com o estabelecimento e certifique-se que a situação já não foi resolvida.</strong></p>\",\n" +
                "    \"primary_action\": {\n" +
                "        \"title\": \"Continuar\",\n" +
                "        \"action\": \"continue\"\n" +
                "    },\n" +
                "    \"secondary_action\": {\n" +
                "        \"title\": \"Fechar\",\n" +
                "        \"action\": \"cancel\"\n" +
                "    },\n" +
                "    \"links\": {\n" +
                "        \"chargeback\": {\n" +
                "            \"href\": \"https://nu-mobile-hiring.herokuapp.com/chargeback\"\n" +
                "        }\n" +
                "    }\n" +
                "}"
    }

    private fun fakeChargebackWithPreventiveBlocking(): String {
        return fakeChargebackResponse().replace(
                "\"autoblock\": false,\n",
                "\"autoblock\": true,\n"
        )
    }

    private fun fakeChargebackResponse(): String {
        return "{\n" +
                "  \"comment_hint\": \"Nos conte o que aconteceu\",\n" +
                "  \"id\": \"fraud\",\n" +
                "  \"title\": \"Não reconheço esta compra\",\n" +
                "  \"autoblock\": false,\n" +
                "  \"reason_details\": [\n" +
                "    {\n" +
                "      \"id\": \"merchant_recognized\",\n" +
                "      \"title\": \"Reconhece o estabelecimento?\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"card_in_possession\",\n" +
                "      \"title\": \"Está com o cartão em mãos?\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"links\": {\n" +
                "    \"block_card\": {\n" +
                "      \"href\": \"https://nu-mobile-hiring.herokuapp.com/card_block\"\n" +
                "    },\n" +
                "    \"unblock_card\": {\n" +
                "      \"href\": \"https://nu-mobile-hiring.herokuapp.com/card_unblock\"\n" +
                "    },\n" +
                "    \"self\": {\n" +
                "      \"href\": \"https://nu-mobile-hiring.herokuapp.com/chargeback\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }
}



