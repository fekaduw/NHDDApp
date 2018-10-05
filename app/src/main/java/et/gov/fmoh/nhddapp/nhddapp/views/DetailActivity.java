package et.gov.fmoh.nhddapp.nhddapp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.utils.SharedPref;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;

public class DetailActivity extends AppCompatActivity //implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPref sharedPref;

    //selected concept
    private NcodConcept concept;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*sharedPref = new SharedPref(this);

        setTheme (sharedPref.loadNightModeState() ? R.style.DarkTheme : R.style.AppTheme);
*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

     /*   if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
*/
        //get the selected concept from the fragment activity
        Bundle bundle = getIntent().getExtras();
        concept = new NcodConcept();
        concept.setDisplay_name(bundle.getString("display_name"));
        concept.setDescriptions(TextUtils.isEmpty(bundle.getString("description"))? "No description available." : bundle.getString("description"));
        concept.setConcept_class(bundle.getString("category"));

        DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        try {
            date = sdf.parse(bundle.getString("version_created_on"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        concept.setVersion_created_on(date.toString());
        concept.set_latest_version(bundle.getBoolean("is_version_latest"));

        //todo: setTitle(bundle.getString("activity_title"));

        loadData();
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

    private void loadData() {
        TextView iconConcept = findViewById(R.id.concept_icon);
        TextView titleTextView = findViewById(R.id.title_detail);
        TextView descTextView = findViewById(R.id.desc_detail);
        TextView categoryTextView = findViewById(R.id.category_detail);
        TextView versionCreatedOnTextView = findViewById(R.id.version_created_on);
        TextView isVersionLatestTextView = findViewById(R.id.is_version_latest);

        iconConcept.setText(concept.getDisplay_name().substring(0,1));
        titleTextView.setText(concept.getDisplay_name());
        descTextView.setText(concept.getDescriptions());
        categoryTextView.setText(concept.getConcept_class());
        versionCreatedOnTextView.setText(concept.getVersion_created_on());
        isVersionLatestTextView.setText(concept.is_latest_version() ? "Latest version" : "Outdates");
    }

/*    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }*/
}
