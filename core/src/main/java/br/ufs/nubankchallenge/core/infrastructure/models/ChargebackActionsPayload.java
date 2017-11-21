package br.ufs.nubankchallenge.core.infrastructure.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by @ubiratanfsoares
 */

public class ChargebackActionsPayload {

    public String title;
    public boolean autoblock;
    @SerializedName("comment_hint") public String hint;
    @SerializedName("reason_details") public List<ReasonDetailPayload> reasons;

    static class ReasonDetailPayload {
        public String id;
        public String title;
    }

}


