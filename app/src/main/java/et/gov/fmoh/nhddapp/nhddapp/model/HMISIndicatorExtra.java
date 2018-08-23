package et.gov.fmoh.nhddapp.nhddapp.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HMISIndicatorExtra extends RealmObject {
    @SerializedName("HMIS-Category-2")
    private String hmisCategory2;

    @SerializedName("HMIS-Category-3")
    private String hmisCategory3;

    @SerializedName("HMIS-Category-1")
    private String hmisCategory1;

    @SerializedName("Interpretation")
    private String interpretation;

    @SerializedName("HMIS-Category-4")
    private String hmisCategory4;

    @SerializedName("Applicable Reporting Units")
    private String applicableReportingUnits;

    @SerializedName("Numerator")
    private String numerator;

    @SerializedName("Denominator")
    private String denominator;

    @SerializedName("Disaggregation")
    private String disaggregation;

    @SerializedName("Reporting Frequency")
    private String reportingFrequency;

    @SerializedName("Multiplier")
    private String multiplier;

  @SerializedName("Primary Sources")
    private String primarySources;
}
