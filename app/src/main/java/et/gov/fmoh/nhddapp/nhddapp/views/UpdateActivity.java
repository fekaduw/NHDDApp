package et.gov.fmoh.nhddapp.nhddapp.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.utils.CONST;
import et.gov.fmoh.nhddapp.nhddapp.utils.DatabaseHelper;
import et.gov.fmoh.nhddapp.nhddapp.utils.HMISIndicatorUpdateManager;
import et.gov.fmoh.nhddapp.nhddapp.utils.NCoDUpdateManager;
import et.gov.fmoh.nhddapp.nhddapp.utils.NetworkUtils;
import et.gov.fmoh.nhddapp.nhddapp.utils.SharedPref;
import et.gov.fmoh.nhddapp.nhddapp.service.NcodDataSyncIntentService;

public class UpdateActivity extends AppCompatActivity {
    @BindView(R.id.update_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.update_status)
    TextView updateTextView;

    @BindView(R.id.btn_update)
    Button btnUpdate;

    @BindView(R.id.internet_status)
    TextView internetStatusTextView;

    //shared preference
    SharedPref sharedPref;

    //Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        setTheme(sharedPref.loadNightModeState() ? R.style.DarkTheme : R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if (isInternetAvailable()) {
            btnUpdateSetup();
        }
    }

    private boolean isInternetAvailable() {
        if (NetworkUtils.isInternetAvailable(this)) {
            internetStatusTextView.setVisibility(View.GONE);
            return true;
        } else {
            internetStatusTextView.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private void btnUpdateSetup() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvailable()) {
                    Toast.makeText(getApplicationContext(), "Update about to begin...", Toast.LENGTH_SHORT).show();
                    //checkForUpdates();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void checkForUpdates() {

        //Internet connection available
        if (isInternetAvailable()) {
            //check permission to access local storage
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CONST.MY_PERMISSIONS_REQUEST);
            }

            //check permission to access to the Internet
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, CONST.MY_PERMISSIONS_REQUEST);
            }

            //check permission to access the network state
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, CONST.MY_PERMISSIONS_REQUEST);
            }

            sharedPref.setNCoDVersion(null);
            sharedPref.setHMISIndicatorVersion(null);

            Intent intent = new Intent(this, NcodDataSyncIntentService.class);
            intent.putExtra("version", sharedPref.getNCoDVersion());
            //intent.putExtra("hmis_version", sharedPref.getHMISIndicatorVersion());

            startService(intent);

            updateNCoD();

            updateHMISIndicator();

/*
        todo:
            DatabaseHelper activityHelper = new DatabaseHelper();
            activityHelper.restartActivity();
*/
        } else {
            Toast.makeText(this, "No Internet connection available to download the latest data.", Toast.LENGTH_LONG).show();
        }
    }

    private void updateNCoD() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... voids) {
                Log.d(CONST.TAG, "Checking file for update");
                NCoDUpdateManager updateManager = new NCoDUpdateManager();
                updateManager.updateNCoDData(getApplicationContext(), sharedPref.getNCoDVersion());

                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String aVoid) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }.execute();
    }

    private void updateHMISIndicator() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... voids) {
                Log.d(CONST.TAG, "Checking file for update");
                HMISIndicatorUpdateManager updateManager = new HMISIndicatorUpdateManager();
                updateManager.updateHMISIndicatorData(getApplicationContext(), sharedPref.getHMISIndicatorVersion());

                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String aVoid) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }.execute();
    }
}