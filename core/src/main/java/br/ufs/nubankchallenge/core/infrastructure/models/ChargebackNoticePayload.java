package br.ufs.nubankchallenge.core.infrastructure.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by @ubiratanfsoares
 */

public class ChargebackNoticePayload {

    public String title;
    public String description;
    @SerializedName("primary_action") public ActionPayload primaryAction;
    @SerializedName("secondary_action") public ActionPayload secondaryAction;

    public static class ActionPayload {
        public String title;
        public String action;
    }

}
