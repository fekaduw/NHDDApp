package et.gov.fmoh.nhddapp.nhddapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConceptVersion {

   private String id;
   private boolean released;
   private boolean retired;
   private String owner;
   private String owner_type;
   private String owner_url;
    private String created_on;
    private String updated_on;
    private String version_url;
    private String url;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner_type() {
        return owner_type;
    }

    public void setOwner_type(String owner_type) {
        this.owner_type = owner_type;
    }

    public String getOwner_url() {
        return owner_url;
    }

    public void setOwner_url(String owner_url) {
        this.owner_url = owner_url;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getVersion_url() {
        return version_url;
    }

    public void setVersion_url(String version_url) {
        this.version_url = version_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
