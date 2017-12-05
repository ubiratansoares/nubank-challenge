package br.ufs.nubankchallenge.core.infrastructure.models

import com.google.gson.annotations.SerializedName

/**
 * Created by @ubiratanfsoares
 */

class ChargebackActionsPayload {

    var title: String? = null
    @SerializedName("comment_hint") var hint: String? = null
    @SerializedName("reason_details") var reasons: List<ReasonDetailPayload>? = null
    var autoblock: Boolean = false

    class ReasonDetailPayload {
        var id: String? = null
        var title: String? = null
    }

}


