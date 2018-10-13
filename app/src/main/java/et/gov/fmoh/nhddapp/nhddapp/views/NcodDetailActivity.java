package et.gov.fmoh.nhddapp.nhddapp.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicatorConcept;
import et.gov.fmoh.nhddapp.nhddapp.utils.DateHelper;
import et.gov.fmoh.nhddapp.nhddapp.utils.GenerateColor;
import et.gov.fmoh.nhddapp.nhddapp.utils.SharedPref;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;

import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.CATEGORY_HMIS_INDICATOR;
import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.CATEGORY_NCOD;

public class NcodDetailActivity extends AppCompatActivity //implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPref sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        setTheme(sharedPref.loadNightModeState() ? R.style.DarkTheme : R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ncod_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        loadNcodData(getIntent().getExtras());
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

    private void loadNcodData(@Nullable Bundle bundle) {
        ImageView iconConcept = findViewById(R.id.concept_icon);
        TextView titleTextView = findViewById(R.id.title_detail);
        TextView categoryTextView = findViewById(R.id.category_name);
        TextView descriptionTextView = findViewById(R.id.description);
        TextView classTextView = findViewById(R.id.class_name);
        TextView versionCreatedOnTextView = findViewById(R.id.version_created_on);
        TextView versionTextView = findViewById(R.id.version);

        GenerateColor<NcodConcept> generateColor = new GenerateColor<>();
        iconConcept.setImageDrawable(generateColor.getTextDrawable(bundle.getString("display_name"), bundle.getInt("color")));

        titleTextView.setText(bundle.getString("display_name"));
        categoryTextView.setText(bundle.getString("categoryName"));
        descriptionTextView.setText(bundle.getString("description"));
        classTextView.setText(bundle.getString("class_name"));
        versionCreatedOnTextView.setText(bundle.getString("version_created_on"));
        versionTextView.setText(bundle.getString("version"));
    }
}
