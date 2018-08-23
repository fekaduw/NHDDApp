package et.gov.fmoh.nhddapp.nhddapp.helpers;

import android.os.Environment;
import android.widget.ProgressBar;

import java.io.File;
import java.security.PublicKey;

public class CONST {
    public static final File LOCAL_STORAGE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    public static final int MY_PERMISSIONS_REQUEST = 100;

    public static final String TAG = "NHDDPocket_TAG";
}
