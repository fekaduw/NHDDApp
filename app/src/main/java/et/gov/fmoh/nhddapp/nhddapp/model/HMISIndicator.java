package et.gov.fmoh.nhddapp.nhddapp.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HMISIndicator extends RealmObject{
    @SerializedName("concepts")
    private RealmList<HMISIndicatorConcept> concepts;

    public static HMISIndicator create (HMISIndicator hmisIndicator){
        HMISIndicator hmisIndicator1 = new HMISIndicator();
        hmisIndicator1 = hmisIndicator;

        return hmisIndicator1;
    }
}
