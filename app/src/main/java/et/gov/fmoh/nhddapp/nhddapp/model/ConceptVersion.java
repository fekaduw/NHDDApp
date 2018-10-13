package et.gov.fmoh.nhddapp.nhddapp.model;

import lombok.Getter;
import lombok.Setter;

public class ConceptVersion {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private boolean released;
    @Getter
    @Setter
    private boolean retired;
    @Getter
    @Setter
    private String owner;
    @Getter
    @Setter
    private String owner_type;
    @Getter
    @Setter
    private String owner_url;
    @Getter
    @Setter
    private String created_on;
    @Getter
    @Setter
    private String updated_on;
    @Getter
    @Setter
    private String version_url;
    @Getter
    @Setter
    private String url;
}
