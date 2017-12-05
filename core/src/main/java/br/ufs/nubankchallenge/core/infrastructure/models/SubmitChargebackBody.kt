package br.ufs.nubankchallenge.core.infrastructure.models

import com.google.gson.annotations.SerializedName

/**
 * Created by @ubiratanfsoares
 */

class SubmitChargebackBody {

    var comment: String? = null
    @SerializedName("reason_details") var details: List<ReasonDetailBody>? = null

    class ReasonDetailBody(var id: String, var response: Boolean)

}
