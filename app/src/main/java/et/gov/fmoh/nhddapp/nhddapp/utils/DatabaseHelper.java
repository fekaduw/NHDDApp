package et.gov.fmoh.nhddapp.nhddapp.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicator;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicatorConcept;
import et.gov.fmoh.nhddapp.nhddapp.model.NCoD;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodExtras;
import io.realm.Realm;
import io.realm.RealmList;
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

    //todo: NCOD categories
    public ArrayList<NcodExtras> getNCoDCategories(Realm realm) {

        //Get data from the database
        RealmResults<NCoD> ncodConcept = realm.where(NCoD.class).findAllAsync();
        try{
            if(ncodConcept!=null) {
                RealmList<NcodConcept> newData = ncodConcept.get(0).getConcepts();
                ArrayList<NcodExtras> ncodExtras = null;
                for (NcodConcept extra: newData) {
                    ncodExtras.add(extra.getNcodExtras());
                }

                return ncodExtras;
            }}catch (Exception ex){
            Log.e(CONST.TAG, "getNCoDCategories(): NCoD related error occurred when trying to fetch data.\n"+ex.getMessage());
            ex.getStackTrace();
            return null;
        }
        return null;
    }

    public ArrayList<NcodConcept> getNCoDConcepts(Realm realm) {

        //Get data from the database
        RealmResults<NCoD> ncodConcept = realm.where(NCoD.class).findAllAsync();
        try{
        if(ncodConcept!=null) {
            RealmList<NcodConcept> newData = ncodConcept.get(0).getConcepts();
            return newData!=null ? (ArrayList<NcodConcept>) realm.copyFromRealm(newData) : null;
        }}catch (Exception ex){
            Log.e(CONST.TAG, "getNCoDConcepts(): NCoD related error occurred when trying to fetch data.\n"+ex.getMessage());
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
            Log.e(CONST.TAG, "getHMISIndicatorConcepts(): HMIS-Indicators related error occurred when trying to fetch data.\n"+ex.getMessage());
            ex.getStackTrace();
            return null;
        }
        return null;
    }

    public static String getCurrentDate(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}