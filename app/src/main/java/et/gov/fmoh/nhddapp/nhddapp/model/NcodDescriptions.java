package et.gov.fmoh.nhddapp.nhddapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

    public class NcodDescriptions extends RealmObject implements Serializable {
        @SerializedName("description")
        public String description;
    }