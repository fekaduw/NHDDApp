package et.gov.fmoh.nhddapp.nhddapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicator;
import et.gov.fmoh.nhddapp.nhddapp.model.NCoD;
import et.gov.fmoh.nhddapp.nhddapp.utils.CONST;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(CONST.DATABASE_NAME)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);


        /*//initializing the realm db
        Realm.init(this);

        //RealmConfiguration config = new RealmConfiguration.Builder().name("nhdd.realm").build();
        RealmConfiguration config = new RealmConfiguration.Builder()
                *//*.initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(HMISIndicator.create(null));
                        realm.copyToRealm(NCoD.create(null));
                    }
                })*//*
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(BuildConfig.VERSION_CODE)
                .name("nhdd.realm")
                .build();

        Realm.setDefaultConfiguration(config);*/

//        try{
//            Realm realm = Realm.getInstance(config);
//            realm.close();
//        }catch(RealmMigrationNeededException e){
//            Realm.deleteRealm(config);
//        }
//        Realm.getInstance(config);
    }
}
