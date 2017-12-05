package br.ufs.nubankchallenge.core.infrastructure.models

import com.google.gson.annotations.SerializedName

/**
 * Created by @ubiratanfsoares
 */

class ChargebackNoticePayload {

    var title: String? = null
    var description: String? = null
    @SerializedName("primary_action") var primaryAction: ActionPayload? = null
    @SerializedName("secondary_action") var secondaryAction: ActionPayload? = null

    class ActionPayload {
        var title: String? = null
        var action: String? = null
    }

}
