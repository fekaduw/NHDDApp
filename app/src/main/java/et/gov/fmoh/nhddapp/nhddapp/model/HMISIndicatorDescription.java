package et.gov.fmoh.nhddapp.nhddapp.model;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HMISIndicatorDescription extends RealmObject {
    private String uuid;
    private String description;
}
