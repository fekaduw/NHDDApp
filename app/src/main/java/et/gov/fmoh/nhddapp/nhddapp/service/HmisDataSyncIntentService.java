package et.gov.fmoh.nhddapp.nhddapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import et.gov.fmoh.nhddapp.nhddapp.helpers.CONST;
import et.gov.fmoh.nhddapp.nhddapp.helpers.HMISIndicatorUpdateManager;

public class HmisDataSyncIntentService extends IntentService {
    public HmisDataSyncIntentService() {
        super("HMIS data update...");
    }

    public HmisDataSyncIntentService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(CONST.TAG, "onStartCommand() of HMISDataSyncIntentService called: Service started...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();



        Log.d(CONST.TAG, "onDestroy() of HMISDataSyncIntentService called: Service finished.");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String version = intent.getStringExtra("version");

        Log.d(CONST.TAG, "onHandleIntent() of HMISDataSyncIntentService called: Checking file for HMIS update");
        HMISIndicatorUpdateManager hmisUpdateManager = new HMISIndicatorUpdateManager();
        hmisUpdateManager.updateHMISIndicatorData(getApplicationContext(), version);
    }
}