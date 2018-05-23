package et.gov.fmoh.nhddapp.nhddapp;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import et.gov.fmoh.nhddapp.nhddapp.fragments.Tab1Fragment;
import et.gov.fmoh.nhddapp.nhddapp.fragments.Tab2Fragment;
/*
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
*/
/*import et.gov.fmoh.nhddapp.nhddapp.model.Concept;
import et.gov.fmoh.nhddapp.nhddapp.service.NhddClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;*/

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

   /* private ListView conceptsListView;
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;*/


   Dialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


  /*      Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.openconceptlab.org/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        NhddClient nhddClient = retrofit.create(NhddClient.class);
        Call<List<Concept>> call = nhddClient.reposForConcept();

        call.enqueue(new Callback<List<Concept>>() {
            @Override
            public void onResponse(Call<List<Concept>> call, Response<List<Concept>> response) {
                List<Concept> concepts = response.body();
                conceptsListView.setAdapter(new ConceptAdaptor(MainActivity.this, concepts));
            }

            @Override
            public void onFailure(Call<List<Concept>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Ouch, error!",Toast.LENGTH_LONG).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setFragment(new DrawFragment()); //init

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dialog = new Dialog(MainActivity.this);

/*        conceptsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view.findViewById(R.id.conceptsListView);
                Toast.makeText(getApplicationContext(), "selected Item Name is " + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
            }
        });*/
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
        getMenuInflater().inflate(R.menu.search_menu, menu);

        //search

        MenuItem item = menu.findItem(R.id.action_search);
        MaterialSearchView searchView = findViewById(R.id.search_view);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
        }
 /*       else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } */
 else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_PACKAGE_NAME, "NHDD Pocket");
            sendIntent.setType("app");
            startActivity(sendIntent);

        } else if (id == R.id.nav_feedback) {
            dialog.setContentView(R.layout.activity_feedback);
            dialog.show();

            Button button_send = dialog.findViewById(R.id.send_button);
            button_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText emailAddress = dialog.findViewById(R.id.feedback_email);
                    EditText opinion = dialog.findViewById(R.id.feedback_textView);

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailAddress.getText().toString()});
                    //intent.putExtra(Intent.EXTRA_CC, new String[] {"andy@whatever.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Mail from app!");
                    intent.putExtra(Intent.EXTRA_TEXT, "OMFG I just sent an email from my app!");

                    try {
                        startActivity(Intent.createChooser(intent, "How to send mail?"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        //do something else
                    }
                    //Toast.makeText(MainActivity.this, emailAddress.getText()+" " +opinion.getText(), Toast.LENGTH_LONG).show();
                }
            });

            Button button_cancel = dialog.findViewById(R.id.cancel_button);
            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment){
        if(fragment!=null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, fragment);
            transaction.commit();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
