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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.helpers.CONST;
import et.gov.fmoh.nhddapp.nhddapp.helpers.HMISIndicatorUpdateManager;
import et.gov.fmoh.nhddapp.nhddapp.helpers.NCoDUpdateManager;
import et.gov.fmoh.nhddapp.nhddapp.helpers.SharedPref;
import et.gov.fmoh.nhddapp.nhddapp.service.NcodDataSyncIntentService;

public class UpdateActivity extends Activity {
    //views
    ProgressBar progressBar;
    TextView updateTextView;

    //shared preference
    SharedPref sharedPref;

    //Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);

        sharedPref = new SharedPref(this);

        progressBar = findViewById(R.id.update_progressBar);
        updateTextView = findViewById(R.id.update_status);

        //context = getApplicationContext();
        checkForUpdates();

        Toast.makeText(this, sharedPref.getNCoDVersion(), Toast.LENGTH_LONG).show();
    }

    @SuppressLint("StaticFieldLeak")
    private void checkForUpdates() {

        //check Internet connection
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //Internet connection available
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
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
            intent.putExtra("ncod_version", sharedPref.getNCoDVersion());
            intent.putExtra("hmis_version", sharedPref.getHMISIndicatorVersion());

            startService(intent);

/*
            updateNCoD();

            updateHMISIndicator();

            DatabaseHelper activityHelper = new DatabaseHelper();
            activityHelper.restartActivity();
*/
        }else
        {
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