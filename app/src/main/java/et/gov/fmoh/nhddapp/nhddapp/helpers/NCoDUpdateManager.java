package et.gov.fmoh.nhddapp.nhddapp.helpers;

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
import et.gov.fmoh.nhddapp.nhddapp.model.NCoD;
import et.gov.fmoh.nhddapp.nhddapp.service.BaseAPI;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static et.gov.fmoh.nhddapp.nhddapp.helpers.DatabaseHelper.getCurrentDate;
import static et.gov.fmoh.nhddapp.nhddapp.helpers.CONST.TAG;

public class NCoDUpdateManager {

    private static BaseAPI baseAPI = BaseAPI.Factory.create();
    private String latestVersion;
    private Realm realm = null;
    private Context context;
    private boolean isDownloadComplete;

    @SuppressLint("StaticFieldLeak")
    public void updateNCoDData(final Context context, final String currentVersion) {

        Log.d(TAG, "***************inside updateNCoDData() in NCoDUpdateManager()***************");

        //set context
        this.context = context;

        //API communication...
        Call<ConceptVersion> version = baseAPI.getNCoDLatestVersion();

        version.enqueue(new Callback<ConceptVersion>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ConceptVersion> call,
                                   final Response<ConceptVersion> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse() in updateNCoDData() called. Server contacted and has file");

                    if (isVersionLatest(currentVersion, response.body())) {

                        Log.d(TAG, "The version on the server is latest. Version: " + latestVersion);

                        //fetch the export data
                        getNCodData(latestVersion);
                    }

                } else {
                    Log.d(TAG, "onResponse() in updateNCoDData() called. Contacting the server failed");
                    latestVersion = null;
                }
            }

            @Override
            public void onFailure(Call<ConceptVersion> call, Throwable t) {
                latestVersion = null;
                Log.e(TAG, "onFailure() called. Error occurred when checking the version of the file to download.");
            }
        });

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

        if (DataTypeConverter.ConvertStringToDate(currentVersion).before(DataTypeConverter.ConvertStringToDate(version.getCreated_on())))
        {
            latestVersion = version.getCreated_on();
            return true;
        }
        return false;
    }

    /*private boolean isVersionLatest(String currentVersion, ConceptVersion version) {
        //the current version number that is locally stored
        float currentVersionNo = 0;

        //the version no. from the OCL server
        float versionNo = 0;

        if (currentVersion != null)
            //this assumes that the version has the format v{some number}. eg. v1.0
            //assign the number part to the currentVersionNo variable
            currentVersionNo = Float.parseFloat(currentVersion.substring(currentVersion.indexOf("v") + 1, currentVersion.length()));

        //assigns the number part of the latest version from
        versionNo = Float.parseFloat(version.getId().substring(version.getId().indexOf("v") + 1, version.getId().length()));
        Log.d(TAG, "Version no. from server: " + versionNo);
        Log.d(TAG, "Current version no.: " + currentVersionNo);

        if (currentVersionNo < versionNo) {
            latestVersion = version.getId();
            return true;
        } else {
            return false;
        }
    }*/


    private void getNCodData(final String currentVersion) {

        Log.d(TAG, "*************** inside getNCoDData() in NCoDUpdateManger() ***************");

        //API communication...
        Call<ResponseBody> concepts = baseAPI.getNCoDExport(currentVersion);
        concepts.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "File being downloaded");

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {

                            isDownloadComplete = DataHelper.storeExportDataToDisk(response.body(), "NCoD");

                            Log.d(TAG, "inside getNCoDData(): Is the file downloaded and saved on disk? " + isDownloadComplete);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            if (isDownloadComplete)
                                try {
                                    Log.d(TAG, "Unzipping the NCoD file ...");
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
                Log.e(TAG, "There was an error in downloading the NCoD file.");
            }
        });
    }

    private void populateData() {
        realm = Realm.getDefaultInstance();
        Log.d(TAG, "Is the realm object closed? " + realm.isClosed());
        try {
            realm.beginTransaction();

            String currentDate = getCurrentDate().replace("-", "");
            File file = new File(CONST.LOCAL_STORAGE, "NCoD_" + currentDate + "_export.json");

            Log.d(TAG, " ********** fileName after unzip : " + file.toString());

            InputStream stream = new FileInputStream(file);

            realm.createObjectFromJson(NCoD.class, stream);

            realm.commitTransaction();

            RealmResults<NCoD> concepts = null;
            concepts = realm.where(NCoD.class).findAllAsync();
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
            Log.e(TAG, "Updating the database failed.\n" + e.getMessage());
        } finally {
            //save the version in the sharedPref
            SharedPref sharedPref = new SharedPref(context);
            sharedPref.setHMISIndicatorVersion(latestVersion);

            if (realm != null)
                realm.close();
        }
    }
}