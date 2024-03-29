package et.gov.fmoh.nhddapp.nhddapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NCoD extends RealmObject {
    private String type;
    private String id;
    @SerializedName("concepts")
    @Expose
    private RealmList<NcodConcept> concepts;

/*
    public static NCoD create (NCoD nCoD){
        NCoD nCoD1 = new NCoD();
        nCoD1 = nCoD;
        return nCoD1;
    }
*/
}