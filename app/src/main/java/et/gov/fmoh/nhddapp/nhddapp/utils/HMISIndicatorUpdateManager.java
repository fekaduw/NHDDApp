package et.gov.fmoh.nhddapp.nhddapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import et.gov.fmoh.nhddapp.nhddapp.model.ConceptVersion;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicator;
import et.gov.fmoh.nhddapp.nhddapp.service.BaseAPI;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.TAG;
import static et.gov.fmoh.nhddapp.nhddapp.utils.DataHelper.deleteFile;

public class HMISIndicatorUpdateManager {
    private static BaseAPI baseAPI = BaseAPI.Factory.create();
    private String latestVersion;
    private Realm realm = null;
    private Context context;
    private boolean isDownloadComplete;

    @SuppressLint("StaticFieldLeak")
    public void updateHMISIndicatorData(final Context context, final String currentVersion) {

        Log.d(TAG, "***************inside updateHMISIndicatorData() in HMISIndicatorUpdateManager()***************");

        //set context
        this.context = context;

        //API communication...
        Call<ConceptVersion> version = baseAPI.getHMISIndicatorLatestVersion();

        version.enqueue(new Callback<ConceptVersion>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ConceptVersion> call,
                                   final Response<ConceptVersion> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse() in updateHMISIndicatorData() called. Server contacted and has file");

                    if (isVersionLatest(currentVersion, response.body())) {
                        latestVersion = response.body().getCreated_on();
                        Log.d(TAG, "The version on the server is latest. Version: " + latestVersion);

                        //fetch the export data
                        getHMISIndicatorsData(response.body().getId());
                    }

                } else {
                    Log.d(TAG, "onResponse() in updateHMISIndicatorData() called. Contacting the server failed");
                    latestVersion = null;
                }
            }

            @Override
            public void onFailure(Call<ConceptVersion> call, Throwable t) {
                latestVersion = null;
                Log.e(TAG, "onFailure() called. Error occurred when checking the version of the HMIS Indicators file to download.\n"+t.getMessage());
            }
        });


        /*        //set context
        this.context = context;

        //API communication...
        Call<ConceptVersion> version = baseAPI.getHMISIndicatorLatestVersion();

        version.enqueue(new Callback<ConceptVersion>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ConceptVersion> call,
                                   final Response<ConceptVersion> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Server contacted and has file");

                    //Todo: replace get(1) by the version stored in the shared preferences

                    if (isVersionLatest(currentVersion, response.body())) {
                        //todo: sharedPref

                        Log.d(TAG, "Version: " + latestVersion);

                        //fetch the export data
                        getHMISIndicatorsData(latestVersion);
                    }

                } else {
                    Log.d(TAG, "Contacting server failed");
                    latestVersion = null;
                }
            }

            @Override
            public void onFailure(Call<ConceptVersion> call, Throwable t) {
                latestVersion = null;
                Log.e(TAG, "Error occurred when downloading file.");
            }
        });

        Log.d(TAG, "Called while data is being processed in the bg. Version: " + latestVersion);*/
    }

    ///currentVersion - the version locally stored in the sharedPreference
    ///version.getCreated_on() - the latest version from the OCL server
    private boolean isVersionLatest(String currentVersion, ConceptVersion version) {
        if (TextUtils.isEmpty(currentVersion)) {
            currentVersion = version.getCreated_on();
            return true;
        }

        //convert the date string to date values

        Log.d(TAG, "Version no. from server: " + version.getCreated_on());
        Log.d(TAG, "Current version no.: " + currentVersion);

        if (DataTypeConverter.convertStringToDate(currentVersion).before(DataTypeConverter.convertStringToDate(version.getCreated_on())))
        {
            latestVersion = version.getCreated_on();
            return true;
        }
        return false;
    }

    private void getHMISIndicatorsData(final String version) {
        Log.d(TAG, "*************** inside getHMISIndicatorsData() in HMISIndicatorUpdateManger() ***************");

        //API communication...
        Call<ResponseBody> concepts = baseAPI.getHMISIndicatorsExport(version);
        concepts.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "File being downloaded");

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {

                            isDownloadComplete = DataHelper.storeExportDataToDisk(response.body(), "HMISIndicators");

                            Log.d(TAG, "inside getHMISIndicatorsData(): Is the file downloaded and saved on disk? " + isDownloadComplete);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            if (isDownloadComplete)
                                try {
                                    Log.d(TAG, "Unzipping the HMIS Indicators file ...");
                                    if (DataHelper.unZipFile()) {
                                        Log.d(TAG, "File successfully unzipped");
                                        populateData();
                                    }
                                } catch (IOException e) {
                                    Log.e(TAG, "File unzip failed");
                                    e.printStackTrace();
                                }
                            super.onPostExecute(aVoid);
                        }
                    }.execute();
                } else {
                    Log.d(TAG, "Server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "There was an error in downloading the HMIS Indicators file.\n"+t.getMessage());
            }
        });

        /*

        //API communication...
        Call<ResponseBody> concepts = baseAPI.getHMISIndicatorsExport(currentVersion);
        concepts.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "File being downloaded");

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            isDownloadComplete = DataHelper.storeExportDataToDisk(response.body(), "HMISIndicators");

                            Log.d(TAG, "File downloaded and saved on disk: " + isDownloadComplete);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            //progressBar.setVisibility(View.GONE);
                            if (isDownloadComplete)
                                try {
                                    if (DataHelper.unZipFile()) {
                                        Log.d(TAG, "File unzip was a success");
                                        populateData();
                                    }
                                } catch (IOException e) {
                                    Log.e(TAG, "File unzip failed");
                                    e.printStackTrace();
                                }
                            super.onPostExecute(aVoid);

                            progressDialog.dismiss();
                        }

                        @Override
                        protected void onPreExecute() {
                            progressDialog = new ProgressDialog(context);
                            progressDialog.setTitle("Download...");
                            progressDialog.setMax(100);
                            progressDialog.setCancelable(false);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        }

                        @Override
                        protected void onProgressUpdate(Void... values) {
                            progressDialog.setProgress(values.length);
                        }
                    }.execute();
                } else {
                    Log.d(TAG, "Server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), "Could not download data now. Try again later.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "There was an error in downloading the file.");
            }
        });
*/
    }

    private void populateData() {
        realm = Realm.getDefaultInstance();
        String currentDate = null;
        File file = null;

        Log.d(TAG, "Is the realm object under HMISIndicatorUpdateManager closed? " + realm.isClosed());
        try {
            realm.beginTransaction();

            currentDate = DateHelper.getCurrentDate().replace("-", "");
            file = new File(CONST.LOCAL_STORAGE, "HMISIndicators_" + currentDate + "_export.json");

            Log.d(TAG, " ********** fileName after unzip : " + file.toString());

            InputStream stream = new FileInputStream(file);

            realm.createObjectFromJson(HMISIndicator.class, stream);

            realm.commitTransaction();

            RealmResults<HMISIndicator> concepts = null;
            concepts = realm.where(HMISIndicator.class).findAllAsync();
            if (concepts != null && concepts.size() >= 0) {
                //fetch the data from the OCL using the API and cache it on the local db
                Log.d(TAG, "Sample data after the database was updated: " + concepts.get(0).getConcepts().get(0).getDisplay_name());

                Log.d(TAG, "Updated database from the JSON file");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if (realm.isInTransaction())
                realm.cancelTransaction();
            e.printStackTrace();
            Log.e(TAG, "Updating the database with HMIS Indicators data failed.\n" + e.getMessage());
        } finally {
            //save the version in the sharedPref
            SharedPref sharedPref = new SharedPref(context);
            sharedPref.setHMISIndicatorVersion(latestVersion);

            deleteFile(new File (CONST.LOCAL_STORAGE,"HMISIndicators_"+currentDate+".zip"), file );

            if (realm != null) {
                realm.close();
            }
        }
    }

        /*
        realm = Realm.getDefaultInstance();

        //realm.beginTransaction();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                try {
                    File file = new File(CONST.LOCAL_STORAGE, "HMISIndicators_export.json");

                    Log.d(TAG, file.toString());
                    InputStream stream = new FileInputStream(file);

                    realm.createObjectFromJson(HMISIndicator.class, stream);

                    realm.commitTransaction();

                    RealmResults<HMISIndicator> concepts = null;
                    concepts = realm.where(HMISIndicator.class).findAll();
                    if (concepts.size() <= 0) {
                        //fetch the data from the OCL using the API and cache it on the local db
                        Log.d(TAG, concepts.get(0).getConcepts().get(0).getDisplay_name());
                    }

                    Log.d(TAG, "Updated database from the JSON file");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    if (realm.isInTransaction())
                        realm.cancelTransaction();
                    e.printStackTrace();
                    Log.e(TAG, "Updating the database failed.\n" + e.getMessage());
                } finally {
                    //save the version in the sharedPref
                    SharedPref sharedPref = new SharedPref(context);
                    sharedPref.setHMISIndicatorVersion(latestVersion);

                    if (realm != null)
                        realm.close();
                }
            }
        });
    }*/
}
