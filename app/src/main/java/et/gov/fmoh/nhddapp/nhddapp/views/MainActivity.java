package et.gov.fmoh.nhddapp.nhddapp.views;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import et.gov.fmoh.nhddapp.nhddapp.DrawFragment;
import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.utils.DatabaseHelper;
import et.gov.fmoh.nhddapp.nhddapp.utils.SharedPref;
import io.realm.Realm;
import et.gov.fmoh.nhddapp.nhddapp.utils.CONST;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPref sharedPref;

    Dialog dialog;

    private DatabaseHelper databaseHelper;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);

        setTheme(sharedPref.loadNightModeState() ? R.style.DarkTheme : R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper();

        //todo: remove after testing
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //check data availability
        //isDataAvailable();

        //initialize the fragments
        setFragment(new DrawFragment());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dialog = new Dialog(MainActivity.this);

        //Log.d(CONST.TAG, "HMIS Version: " + sharedPref.getHMISIndicatorVersion() + "/nNCod version: " + sharedPref.getNCoDVersion());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchView searchView = findViewById (R.id.search_view);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            dialog.setContentView(R.layout.custom_popup);
            dialog.show();
            //Toast.makeText(MainActivity.this, "Hello World", Toast.LENGTH_LONG).show();
            //setFragment(new DrawFragment());
        } else if (id == R.id.nav_update) {
            Intent updateIntent = new Intent(this, UpdateActivity.class);
            startActivity(updateIntent);
        } else if (id == R.id.nav_setting) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);

        } else if (id == R.id.nav_help) {
            Intent helpSupportIntent = new Intent(this, HelpSupportActivity.class);
            startActivity(helpSupportIntent);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Send your request to apps@fmoh.gov.et. to get the latest version of the 'NHDD Pocket' mobile app");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_feedback) {
            dialog.setContentView(R.layout.activity_feedback);

            Button button_send = dialog.findViewById(R.id.send_button);
            button_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //EditText emailAddress = dialog.findViewById(R.id.feedback_email);
                    EditText opinion = dialog.findViewById(R.id.feedback_textView);

                    if (TextUtils.isEmpty(opinion.getText())) {
                        opinion.setError("Please enter your email address.");
                    } else {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");

                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"fekaduw@gmail.com"});//todo change
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Mail from NHDDPocket mobile app!");
                        intent.putExtra(Intent.EXTRA_TEXT, opinion.getText());

                        try {
                            startActivity(Intent.createChooser(intent, "Select the email system..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            ex.printStackTrace();
                            Log.d(CONST.TAG, "Error encountered while sending your feedback.");
                        }
                    }
                }
            });

            Button button_cancel = dialog.findViewById(R.id.cancel_button);
            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, fragment);
            transaction.commit();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void isDataAvailable() {
        /*//check data availability
        if (!databaseHelper.isNCODDataAvailable(realm)) {
            Log.d(CONST.TAG, "NCoD data not available; update will start...");
            Intent intent = new Intent(this, NcodDataSyncIntentService.class);
            intent.putExtra("version", sharedPref.getNCoDVersion());
            startService(intent);
        }

        if (!databaseHelper.isHMISDataAvailable(realm)) {
            Log.d(CONST.TAG, "HMIS data not available. Updating to start...");
            Intent intent = new Intent(this, HmisDataSyncIntentService.class);
            intent.putExtra("version", sharedPref.getHMISIndicatorVersion());
            startService(intent);
        }*/
    }
}
