package et.gov.fmoh.nhddapp.nhddapp.views;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;
import et.gov.fmoh.nhddapp.nhddapp.utils.GenerateColor;
import et.gov.fmoh.nhddapp.nhddapp.utils.SharedPref;

import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.CATEGORY_HMIS_INDICATOR;
import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.CATEGORY_NCOD;

public class HmisDetailActivity extends AppCompatActivity //implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPref sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        setTheme(sharedPref.loadNightModeState() ? R.style.DarkTheme : R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hmis_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        loadHmisData(getIntent().getExtras());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadHmisData(Bundle bundle) {
        ImageView iconConcept = findViewById(R.id.concept_icon);
        TextView titleTextView = findViewById(R.id.title_detail);
        TextView category1TextView = findViewById(R.id.category_name1);
        TextView category2TextView = findViewById(R.id.category_name2);
        TextView category3TextView = findViewById(R.id.category_name3);
        TextView category4TextView = findViewById(R.id.category_name4);

        TextView descriptionTextView = findViewById(R.id.description);
        TextView classTextView = findViewById(R.id.class_name);
        TextView sourceTextView = findViewById(R.id.source);
        TextView datatypeTextView = findViewById(R.id.data_type);
        TextView numeratorTextView = findViewById(R.id.numerator);
        TextView denominatorTextView = findViewById(R.id.denominator);
        TextView disaggregationTextView = findViewById(R.id.disaggregation);
        TextView reportingFrequencyTextView = findViewById(R.id.reporting_frequency);
        TextView multiplierTextView = findViewById(R.id.multiplier);
        TextView primarySourcesTextView = findViewById(R.id.primary_sources);
        TextView applicableReportingUnitsTextView = findViewById(R.id.applicable_reporting_units);
        TextView versionCreatedOnTextView = findViewById(R.id.version_created_on);
        TextView versionTextView = findViewById(R.id.version);

        GenerateColor<NcodConcept> generateColor = new GenerateColor<>();
        iconConcept.setImageDrawable(generateColor.getTextDrawable(bundle.getString("categoryName"), bundle.getInt("color")));
        titleTextView.setText(bundle.getString("display_name"));
        category1TextView.setText(bundle.getString("hmisCategory1"));
        category2TextView.setText(bundle.getString("hmisCategory2"));
        category3TextView.setText(bundle.getString("hmisCategory3"));
        category4TextView.setText(bundle.getString("hmisCategory4"));

        descriptionTextView.setText(bundle.getString("description"));
        classTextView.setText(bundle.getString("concept_class"));

        sourceTextView.setText(bundle.getString("source"));
        datatypeTextView.setText(bundle.getString("datatype"));
        numeratorTextView.setText(bundle.getString("numerator"));
        denominatorTextView.setText(bundle.getString("denominator"));
        disaggregationTextView.setText(bundle.getString("disaggregation"));
        reportingFrequencyTextView.setText(bundle.getString("reportingFrequency"));
        multiplierTextView.setText(bundle.getString("multiplier"));
        primarySourcesTextView.setText(bundle.getString("primarySources"));
        applicableReportingUnitsTextView.setText(bundle.getString("applicableReportingUnits"));
        versionCreatedOnTextView.setText(bundle.getString("version_created_on"));
        versionTextView.setText(bundle.getString("version"));
    }
}
