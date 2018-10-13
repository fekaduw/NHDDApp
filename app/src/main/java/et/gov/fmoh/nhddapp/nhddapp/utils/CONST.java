package et.gov.fmoh.nhddapp.nhddapp.utils;

import android.os.Environment;

import java.io.File;

public class CONST {
    public static final File LOCAL_STORAGE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    public static final int MY_PERMISSIONS_REQUEST = 100;

    public static final String TAG = "NHDDPocket_TAG";

    public static final String DATABASE_NAME = "NHDDPocketDb.realm";
    public static final String CATEGORY_NCOD = "NCOD";
    public static final String CATEGORY_HMIS_INDICATOR = "HMIS_INDICATOR";


}
