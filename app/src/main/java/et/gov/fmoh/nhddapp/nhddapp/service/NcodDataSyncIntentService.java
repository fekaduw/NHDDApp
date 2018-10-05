package et.gov.fmoh.nhddapp.nhddapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import et.gov.fmoh.nhddapp.nhddapp.utils.CONST;
import et.gov.fmoh.nhddapp.nhddapp.utils.NCoDUpdateManager;

public class NcodDataSyncIntentService extends IntentService {
    public NcodDataSyncIntentService() {
        super("NCoD data update...");
    }

    public NcodDataSyncIntentService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(CONST.TAG, "onStartCommand() of NCoDDataSyncIntentService called: service started...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(CONST.TAG, "onDestroy() of NCoDDataSyncIntentService called: Service finished.");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String version = intent.getStringExtra("version");

        Log.d(CONST.TAG, "onHandleIntent() of NCoDDataSyncIntentService called: Checking file for NCOD update with version: " + version);
        NCoDUpdateManager ncodUpdateManager = new NCoDUpdateManager();
        ncodUpdateManager.updateNCoDData(getApplicationContext(), version);
    }
}