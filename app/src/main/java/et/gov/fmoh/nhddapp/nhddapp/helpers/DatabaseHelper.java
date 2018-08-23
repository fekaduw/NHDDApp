package et.gov.fmoh.nhddapp.nhddapp.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicator;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicatorConcept;
import et.gov.fmoh.nhddapp.nhddapp.model.NCoD;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;
import et.gov.fmoh.nhddapp.nhddapp.service.HmisDataSyncIntentService;
import et.gov.fmoh.nhddapp.nhddapp.service.NcodDataSyncIntentService;
import et.gov.fmoh.nhddapp.nhddapp.views.MainActivity;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class DatabaseHelper {

    public boolean isNCODDataAvailable(Realm realm) {

        RealmResults<NCoD> concept = realm.where(NCoD.class).findAllAsync();

        return concept != null && concept.size() > 0 && concept.get(0).getConcepts().size() > 0;

    }

    public boolean isHMISDataAvailable(Realm realm) {
        RealmResults<HMISIndicator> concept = realm.where(HMISIndicator.class).findAll();

        return concept != null && concept.size() > 0 && concept.get(0).getConcepts().size() > 0;
    }

    public ArrayList<NcodConcept> getNCoDConcepts(Realm realm) {

        //Get data from the database
        RealmResults<NCoD> ncodConcept = realm.where(NCoD.class).findAllAsync();
        try{
        if(ncodConcept!=null) {
            RealmList<NcodConcept> newData = ncodConcept.get(0).getConcepts();
            return newData!=null ? (ArrayList<NcodConcept>) realm.copyFromRealm(newData) : null;
        }}catch (Exception ex){
            Log.e(CONST.TAG, "getNCoDConcepts(): NCoD related error occurred when trying to fetch data");
            ex.getStackTrace();
            return null;
        }
        return null;
    }

    public ArrayList<HMISIndicatorConcept> getHMISIndicatorConcepts(Realm realm) {

        //Get data from the database
        RealmResults<HMISIndicator> hmisConcept = realm.where(HMISIndicator.class).findAllAsync();
        try{
            if(hmisConcept!=null) {
                RealmList<HMISIndicatorConcept> newData = hmisConcept.get(0).getConcepts();
                return newData!=null ? (ArrayList<HMISIndicatorConcept>) realm.copyFromRealm(newData) : null;
            }}catch (Exception ex){
            Log.e(CONST.TAG, "getHMISIndicatorConcepts(): HMIS-Indicators related error occurred when trying to fetch data.");
            ex.getStackTrace();
            return null;
        }
        return null;
    }

    public static String getCurrentDate(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}