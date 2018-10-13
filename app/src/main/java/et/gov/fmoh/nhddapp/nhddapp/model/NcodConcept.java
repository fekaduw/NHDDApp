package et.gov.fmoh.nhddapp.nhddapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NcodConcept extends RealmObject implements Serializable
{
    @PrimaryKey
    private String uuid;
    private String id;
    private String datatype;
    private String concept_class;
    private String display_name;
    //private String descriptions;
    private String source;
    private String version;
    private String created_on;
    private String updated_on;
    private String version_created_on;
    private String version_created_by;
    @SerializedName("extras")
    private NcodExtras extras;
    private String mappings;
    private boolean is_latest_version;
}