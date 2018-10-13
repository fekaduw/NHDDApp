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
    @SerializedName("ICD-10 Chapter")
    private String iCD10Block;

    @SerializedName("ICD-10 Block")
    private String iCD10Chapter;

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NcodExtras)) {
            return false;
        }
        NcodExtras other = (NcodExtras) obj;
        return this.getICD10Chapter().equals(other.getICD10Chapter());
    }

    public int hashCode() {
        return getICD10Chapter().hashCode();
    }
}
