package et.gov.fmoh.nhddapp.nhddapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;
import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.utils.SharedPref;

public class SettingsActivity extends AppCompatActivity {
    SharedPref sharedPref;

    @BindView(R.id.nightMode_switch)
    Switch aSwitchNightMode;
    @BindView(R.id.ncod_type)
    Spinner spinnerNcodType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);

        setTheme(sharedPref.loadNightModeState() ? R.style.DarkTheme : R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if (sharedPref.loadNightModeState()) {
            aSwitchNightMode.setChecked(true);
        }

        setupNightMode();

        setupNcodType();
    }

    private void setupNightMode() {
        aSwitchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPref.setNightModeState(isChecked);
                restartApp();
            }
        });
    }

    private void setupNcodType() {
        String[] ncodTypes = getResources().getStringArray(R.array.ncod_types);

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this,R.layout.spinner_item, ncodTypes);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);

        spinnerNcodType.setAdapter(spinnerArrayAdapter);

        String ncodTypeSelected = sharedPref.getNcodType();
        if(ncodTypeSelected!=null)
        {
            for (int i=0;i<ncodTypes.length; i++) {
                if(ncodTypes[i].equalsIgnoreCase(ncodTypeSelected)){
                    spinnerNcodType.setSelection(i);
                    break;
                }
            }
        }

        spinnerNcodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=-1){
                    //Toast.makeText(getApplicationContext(), "You selected: "+ parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
                    sharedPref.setNcodType(spinnerNcodType.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void restartApp() {
        //todo: open the MainActivity?
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
