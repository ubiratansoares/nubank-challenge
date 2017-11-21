package br.ufs.nubankchallenge.core.infrastructure.models;

import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.Nullable;

/**
 * Created by @ubiratanfsoares
 */

public class ChargebackNoticePayload {

    @Nullable public String title;
    @Nullable public String description;
    @Nullable @SerializedName("primary_action") public ActionPayload primaryAction;
    @Nullable @SerializedName("secondary_action") public ActionPayload secondaryAction;

    public static class ActionPayload {
        public String title;
        public String action;
    }

}
