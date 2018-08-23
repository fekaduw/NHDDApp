package et.gov.fmoh.nhddapp.nhddapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NcodExtras extends RealmObject implements Serializable {
    @SerializedName("ICD10-Chapter")
    private String ICD10_Block;

    @SerializedName("ICD10-Chapter")
    private String ICD10_Chapter;
}
