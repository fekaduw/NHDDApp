package et.gov.fmoh.nhddapp.nhddapp.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
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
import et.gov.fmoh.nhddapp.nhddapp.model.ConceptVersion;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;
import et.gov.fmoh.nhddapp.nhddapp.service.ApiClient;
import et.gov.fmoh.nhddapp.nhddapp.utils.CONST;
import et.gov.fmoh.nhddapp.nhddapp.utils.DataTypeConverter;
import et.gov.fmoh.nhddapp.nhddapp.utils.DataUtils;
import et.gov.fmoh.nhddapp.nhddapp.utils.NetworkUtils;
import et.gov.fmoh.nhddapp.nhddapp.utils.SharedPref;
import et.gov.fmoh.nhddapp.nhddapp.utils.UpdateData;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.val;

import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.CATEGORY_HMIS_INDICATOR;
import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.CATEGORY_NCOD;
import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.TAG;

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

    private Disposable disposable;

    private boolean INTERNET_IS_AVAILABLE;

    @Override
    protected void onDestroy() {
    /*    if (!disposable.isDisposed())
            disposable.dispose();*/

        super.onDestroy();
    }

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

        INTERNET_IS_AVAILABLE = isInternetAvailable();

        /*if (INTERNET_IS_AVAILABLE) {*/
        btnUpdate.setEnabled(true);
        checkForPermissions();
       /* } else {
            btnUpdate.setEnabled(false);
        }*/

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                if (INTERNET_IS_AVAILABLE) {
                    updateTextView.setText("Update about to begin...");

                    showProgressBar(true);

                    sharedPref.setNCoDVersion(null);
                    sharedPref.setHMISIndicatorVersion(null);

                    //updateNCoD();

                    //todo: updateHMISIndicator();


                }*/
                showProgressBar(true);
                importData();
            }
        });
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

    @SuppressLint("StaticFieldLeak")
    private void checkForPermissions() {
        if (INTERNET_IS_AVAILABLE) {
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

            /*Intent intent = new Intent(this, NcodDataSyncIntentService.class);
            intent.putExtra("ncodVersion", sharedPref.getNCoDVersion());
            intent.putExtra("hmisVersion", sharedPref.getHMISIndicatorVersion());

            startService(intent);*/

/*
        todo:
            DatabaseHelper activityHelper = new DatabaseHelper();
            activityHelper.restartActivity();
*/
        } else {
            Toast.makeText(this, "No Internet connection available to download the latest data.", Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressBar(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private boolean isVersionLatest(ConceptVersion version, String type) {
        if (version == null)
            return false;

        if (type.equalsIgnoreCase(CATEGORY_NCOD)) {
            if (sharedPref.getNCoDVersion() == null)
                return true;
            else if (DataTypeConverter.convertStringToDate(version.getCreated_on()).after(DataTypeConverter.convertStringToDate(sharedPref.getNCoDVersion()))) {
                return true;
            } else {
                return false;
            }
        } else if (type.equalsIgnoreCase(CATEGORY_HMIS_INDICATOR)) {
            if (sharedPref.getHMISIndicatorVersion() == null)
                return true;
            else if (DataTypeConverter.convertStringToDate(version.getCreated_on()).after(DataTypeConverter.convertStringToDate(sharedPref.getHMISIndicatorVersion()))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

        //return false;
    }

    private void importData() {
        Observable.concat(updateNCoD(), updateHMIS()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext() called...");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "There are some errors.");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete() called. Data successfully added.");
                showProgressBar(false);
            }
        });
    }

    private Observable updateNCoD() {
        return Observable.create(emitter -> {
            DataUtils.importeNcodData(getApplicationContext(), getResources());

            emitter.onNext("NCOD Data successfully added");
            emitter.onComplete();

        /*ConceptVersion version = ApiClient.getInstance().getNcodVersion().blockingFirst();

        if (isVersionLatest(version, CATEGORY_NCOD)) {
             ApiClient.getInstance().getNcodExport(version.getId())
                     .switchMap(responseBodyResponse ->
                        DataUtils.storeExportDataToDisk(responseBodyResponse.body(), "NCoD")
                    )
                     .switchMap(fileName -> DataUtils.unZipFile(fileName))
                     .subscribe(new Observer<Boolean>() {
                         @Override
                         public void onSubscribe(Disposable d) {

                         }

                         @Override
                         public void onNext(Boolean aBoolean) {
                             DataUtils.importeNcodData(getApplicationContext(), version.getCreated_on(), getResources());
                         }

                         @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onComplete() {

                }
            });
        }*/

        /*new UpdateData().getNcodData(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(CONST.TAG, o.toString());
                    }



                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<NCoD> ncodRepos) {
                        try {
                            Toast.makeText(getApplicationContext(), "Loading data..." + ncodRepos.get(0).getConcepts().get(0).getDisplay_name(), Toast.LENGTH_LONG).show();
                            //adapter.setNcodRepos(ncodRepos.get(0).getConcepts());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(CONST.TAG, "Error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // update the adapter with restaurants
                        updateTextView.setText("NCoD data successfully updated.");
                        showProgressBar(false);
                    }
                });*/
        });
    }

    private Observable updateHMIS() {
        return Observable.create(emitter -> {
            DataUtils.importeHMISData(getApplicationContext(), getResources());

            emitter.onNext("HMIS Data successfully added");
            emitter.onComplete();

        /*ConceptVersion version = ApiClient.getInstance().getNcodVersion().blockingFirst();

            if (isVersionLatest(version, CATEGORY_HMIS_INDICATOR)) {
            ApiClient.getInstance().getNcodExport(version.getId())
                    .switchMap(responseBodyResponse ->
                            DataUtils.storeExportDataToDisk(responseBodyResponse.body(), "NCoD")
                    )
                    .switchMap(fileName -> DataUtils.unZipFile(fileName))
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            DataUtils.importeHMISData(getApplicationContext(), version.getCreated_on(),getResources());
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }*/

        /*new UpdateData().getNcodData(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(CONST.TAG, o.toString());
                    }



                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<NCoD> ncodRepos) {
                        try {
                            Toast.makeText(getApplicationContext(), "Loading data..." + ncodRepos.get(0).getConcepts().get(0).getDisplay_name(), Toast.LENGTH_LONG).show();
                            //adapter.setNcodRepos(ncodRepos.get(0).getConcepts());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(CONST.TAG, "Error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // update the adapter with restaurants
                        updateTextView.setText("NCoD data successfully updated.");
                        showProgressBar(false);
                    }
                });*/
        });
    }
    /*@SuppressLint("StaticFieldLeak")
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

    @SuppressLint("StaticFieldLeak")
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
    }*/
}