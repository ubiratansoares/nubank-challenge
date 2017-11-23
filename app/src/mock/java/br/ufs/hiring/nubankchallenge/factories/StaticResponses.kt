package br.ufs.hiring.nubankchallenge.factories

/**
 *
 * Created by @ubiratanfsoares
 *
 */

fun chargebackNoticeResponse() : String {
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