package et.gov.fmoh.nhddapp.nhddapp.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HMISIndicatorConcept extends RealmObject {
    private String uuid;
    private String id;
    private String datatype;
    private String concept_class;
    private String display_name;

   /* @SerializedName("descriptions")
    private RealmList<HMISIndicatorDescription> descriptions;
*/
    private String source;
    private String version;
    private String created_on;
    private String updated_on;
    private String version_created_on;
    private String version_created_by;

  /*  @SerializedName("extras")
    private RealmList<HMISIndicatorExtra> extras;
*/
    private String mappings;
    private boolean is_latest_version;
}
