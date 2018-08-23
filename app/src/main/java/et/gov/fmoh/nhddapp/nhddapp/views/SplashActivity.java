package et.gov.fmoh.nhddapp.nhddapp.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.helpers.CONST;
import et.gov.fmoh.nhddapp.nhddapp.helpers.SharedPref;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private  SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPref = new SharedPref(this);

        checkForPermission();

        /*new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                updateData();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //super.onPostExecute(aVoid);
                Intent i = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(i);
            }

            @Override
            protected void onPreExecute() {
                Log.d("Splash", "PreExecute() called...");
                //super.onPreExecute();
                //checkForPermission();
            }
        }.execute();
*/
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    private void checkForPermission() {
        //check Internet connection
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //Internet connection available
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
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

        } else {
            Toast.makeText(this, "No Internet connection available to download the latest data.", Toast.LENGTH_LONG).show();
        }
    }
}
