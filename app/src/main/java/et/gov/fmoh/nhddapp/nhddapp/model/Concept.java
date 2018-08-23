package et.gov.fmoh.nhddapp.nhddapp.model;

import java.util.List;

public interface Concept<T> {
    public List<T> getConcepts();
    public  String getDisplay_name();
    public  String getConcept_class();
    public  String getDescriptions();
    public  String getVersion_created_on();
    public  Boolean is_latest_version();
}
