package et.gov.fmoh.nhddapp.nhddapp.utils;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicator;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicatorConcept;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicatorExtra;
import et.gov.fmoh.nhddapp.nhddapp.model.NCoD;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodExtras;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.TAG;

public class DatabaseHelper<T> {
    Realm realm;

    public DatabaseHelper() {
        realm = Realm.getDefaultInstance();
    }

    public boolean isNCODDataAvailable(Realm realm) {

        RealmResults<NCoD> concept = realm.where(NCoD.class).findAllAsync();

        return concept != null && concept.size() > 0 && concept.get(0).getConcepts().size() > 0;
    }

    public boolean isHMISDataAvailable(Realm realm) {
        RealmResults<HMISIndicator> concept = realm.where(HMISIndicator.class).findAll();

        return concept != null && concept.size() > 0 && concept.get(0).getConcepts().size() > 0;
    }


    public ArrayList<NcodExtras> getNCoDCategories(Realm realm) {
        realm = Realm.getDefaultInstance();

        //Get data from the database
        RealmResults<NCoD> ncod = realm.where(NCoD.class).findAll();
        try {
            if (ncod != null && ncod.size() > 0) {
                assert ncod.get(0) != null;
                RealmList<NcodConcept> newData = ncod.get(0).getConcepts();

                RealmList<NcodExtras> ncodExtras = new RealmList<>();
                for (NcodConcept extra : newData) {
                    if (extra != null)
                        ncodExtras.add(extra.getExtras());
                }

                ArrayList<NcodExtras> extras = new ArrayList<>();
                for (NcodExtras ext : ncodExtras) {
                    if (TextUtils.isEmpty(ext.getICD10Block()) || TextUtils.isEmpty(ext.getICD10Chapter()))
                        continue;
                    if (!extras.contains(ext)) {
                        extras.add(ext);
                    }
                }

                Log.d(TAG, "The size of the Ncod categories is: " + extras.size());

                return extras;
            }
        } catch (Exception ex) {
            Log.e(CONST.TAG, "getNCoDCategories(): NCoD related error occurred when trying to fetch data.\n" + ex.getMessage());
            ex.getStackTrace();
            return null;
        }

        return null;
    }

    public ArrayList<HMISIndicatorExtra> getHMISCategories(Realm realm) {
        realm = Realm.getDefaultInstance();

        //Get data from the database
        RealmResults<HMISIndicator> hmis = realm.where(HMISIndicator.class).findAll();
        try {
            if (hmis != null && hmis.size() > 0) {
                assert hmis.get(0) != null;
                RealmList<HMISIndicatorConcept> newData = hmis.get(0).getConcepts();

                RealmList<HMISIndicatorExtra> hmisExtras = new RealmList<>();
                for (HMISIndicatorConcept extra : newData) {
                    if (extra != null)
                        hmisExtras.add(extra.getExtras());
                }

                ArrayList<HMISIndicatorExtra> extras = new ArrayList<>();
                for (HMISIndicatorExtra ext : hmisExtras) {
                    if (TextUtils.isEmpty(ext.getHmisCategory1()))
                        continue;
                    if (!extras.contains(ext)) {
                        extras.add(ext);
                    }
                }

                Log.d(TAG, "The size of the HMIS categories is: " + extras.size());

                return extras;
            }
        } catch (Exception ex) {
            Log.e(CONST.TAG, "getHMISCategories(): HMIS related error occurred when trying to fetch data.\n" + ex.getMessage());
            ex.getStackTrace();
            return null;
        }

        return null;
    }

    public ArrayList<NcodConcept> getNCoDConcepts() {

        //Get data from the database
        RealmResults<NCoD> concept = realm.where(NCoD.class).findAll();//findAllAsync()
        Log.d(TAG, "No. of concepts: " + concept.size());

        try {
            if (concept.size() > 0) {
                RealmList<NcodConcept> newData = concept.get(0).getConcepts();
                return newData != null ? (ArrayList<NcodConcept>) realm.copyFromRealm(newData) : null;
            }
        } catch (Exception ex) {
            Log.e(TAG, "getNCoDConcepts(): NCoD related error occurred when trying to fetch data.\n" + ex.getMessage());
            ex.getStackTrace();
            return null;
        }
        Log.d(TAG, "Found no ncod data in the database!");

        return null;
    }

    public ArrayList<NcodConcept> getNcodConcepts(String categoryName) {
        realm = Realm.getDefaultInstance();

        RealmResults<NCoD> concepts = realm.where(NCoD.class).findAll();
        Log.d("Test", "No. of concepts: " + concepts.size());
        if (concepts.size() > 0) {
            Log.d(TAG, "Found: " + concepts.get(0).getConcepts().size() + " concepts in the database");
            Log.d(TAG, "Category name: " + categoryName);

            RealmList<NcodConcept> ncodRepos = concepts.get(0).getConcepts();
            ArrayList<NcodConcept> result = new ArrayList<>();
            for (NcodConcept r : ncodRepos) {
                Log.d("Realm", "Current category: " + r.getExtras().getICD10Chapter());

                if (!TextUtils.isEmpty (r.getExtras().getICD10Chapter()) && r.getExtras().getICD10Chapter().equals(categoryName)) {
                    result.add(r);
                }
            }
            return result;
        } else {
            Log.d(TAG, "Found no concepts in the database!");
            return null;
        }
    }

    public ArrayList<HMISIndicatorConcept> getHmisConcepts(String categoryName) {
        realm = Realm.getDefaultInstance();

        RealmResults<HMISIndicator> concepts = realm.where(HMISIndicator.class).findAll();
        Log.d("Test", "No. of concepts: " + concepts.size());
        if (concepts.size() > 0) {
            Log.d("Realm", "Found: " + concepts.get(0).getConcepts().size() + " concepts in the database");
            Log.d("Realm", "Category name: " + categoryName);

            RealmList<HMISIndicatorConcept> hmisRepos = concepts.get(0).getConcepts();
            ArrayList<HMISIndicatorConcept> result = new ArrayList<>();
            for (HMISIndicatorConcept r : hmisRepos) {
                Log.d("Realm", "Current category: " + r.getExtras().getHmisCategory1());

                if (!TextUtils.isEmpty(r.getExtras().getHmisCategory1()) && r.getExtras().getHmisCategory1().equals(categoryName)) {
                    result.add(r);
                }
            }
            return result;
        } else {
            Log.d(TAG, "Found no concepts in the database!");
            return null;
        }
    }

    public ArrayList<HMISIndicatorConcept> getHMISIndicatorConcepts(Realm realm) {

        //Get data from the database
        RealmResults<HMISIndicator> concept = realm.where(HMISIndicator.class).findAll();//findAllAsync();
        Log.d(TAG, "There are " + concept.size() + " hmis concepts in the database");
        try {
            if (concept.size() > 0) {
                RealmList<HMISIndicatorConcept> newData = concept.get(0).getConcepts();
                return newData != null ? (ArrayList<HMISIndicatorConcept>) realm.copyFromRealm(newData) : null;
            }
        } catch (Exception ex) {
            Log.e(TAG, "getHMISIndicatorConcepts(): HMIS-Indicators related error occurred when trying to fetch data.\n" + ex.getMessage());
            ex.getStackTrace();
            return null;
        }

        return null;
    }


}