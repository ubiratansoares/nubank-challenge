package br.ufs.nubankchallenge.core.infrastructure.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.annotations.Nullable;

/**
 * Created by @ubiratanfsoares
 */

public class ChargebackActionsPayload {

    @Nullable public String title;
    @Nullable @SerializedName("comment_hint") public String hint;
    @Nullable @SerializedName("reason_details") public List<ReasonDetailPayload> reasons;
    public boolean autoblock;

    public static class ReasonDetailPayload {
        public String id;
        public String title;
    }

}


