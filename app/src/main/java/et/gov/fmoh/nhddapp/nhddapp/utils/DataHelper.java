package et.gov.fmoh.nhddapp.nhddapp.utils;

import android.support.annotation.Nullable;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicator;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicatorConcept;
import et.gov.fmoh.nhddapp.nhddapp.model.NCoD;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.ResponseBody;

import static et.gov.fmoh.nhddapp.nhddapp.utils.DatabaseHelper.getCurrentDate;

public class DataHelper {

    private static String fileName;

    public static boolean storeExportDataToDisk(ResponseBody body, String dataType) {
        try {
            Log.d(CONST.TAG, "************ storeExportDataToDiskAbout() called **************");
            String currentDate = getCurrentDate();

            fileName = dataType.concat("_"+ currentDate.replace("-",""));
            Log.d(CONST.TAG, "*********** FileName : " + fileName);

                 File downloadFile = new File(
                        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"NCod.zip");

                        CONST.LOCAL_STORAGE,dataType.isEmpty() ? "unknown" : fileName + ".zip");
            Log.d(CONST.TAG, "File location identified: "+ downloadFile);

            InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    byte[] fileReader = new byte[4096];

                    long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(downloadFile);

                    while (true) {
                        int read = inputStream.read(fileReader);

                        if (read == -1) {
                            break;
                        }

                        outputStream.write(fileReader, 0, read);

                        fileSizeDownloaded += read;

                        Log.d(CONST.TAG, "Size of file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                    }

                    outputStream.flush();

                    return true;
                } catch (IOException e) {
                    Log.d(CONST.TAG, "File could not be downloaded/stored\n"+e.getStackTrace());

                    return false;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                Log.d(CONST.TAG, "File could not be downloaded/stored");

                return false;
            }
        }

    public static void unzip(String zipFile, String location) {
        try {
            FileInputStream inputStream = new FileInputStream(zipFile);
            ZipInputStream zipStream = new ZipInputStream(inputStream);
            ZipEntry zEntry = null;
            while ((zEntry = zipStream.getNextEntry()) != null) {
                Log.d(CONST.TAG, "Unzipping " + zEntry.getName() + " at "
                        + location);

                if (zEntry.isDirectory()) {
                    String path = location + File.separator + zEntry.getName();
                    handleDirectory(path);
                } else {
                    Log.d(CONST.TAG, "****** Unzip the file in this location: " + location + "/" + fileName + "_" + zEntry.getName() + ".zip");
                    FileOutputStream fout = new FileOutputStream(
                            location + "/" + fileName + "_" + zEntry.getName());

                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zipStream.read(buffer)) != -1) {
                        bufout.write(buffer, 0, read);
                    }

                    zipStream.closeEntry();
                    bufout.close();
                    fout.close();
                }
            }
            zipStream.close();
            Log.d(CONST.TAG, "Unzipping complete. path :  " + location);
        } catch (Exception e) {
            Log.d(CONST.TAG, "Unzipping failed");
            e.printStackTrace();
        }
    }

    public static void handleDirectory(String location) {
        File f = new File(location);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    public static boolean unZipFile() throws IOException {
        //String backupDBPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/NCod.zip";
        String backupDBPath = CONST.LOCAL_STORAGE + "/" + fileName+".zip";

        //File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File sd = CONST.LOCAL_STORAGE;
        if (sd.canWrite()) {
            final File backupDBFolder = new File(sd.getPath());
            unzip(backupDBPath, backupDBFolder.getPath());

            return true;
        }
        return false;
    }

    public static void deleteFile(File zipFile, File exportFile) {
        if(zipFile!=null)
            zipFile.delete();

        if(exportFile!=null)
            exportFile.delete();
    }

    @Nullable
    public static ArrayList<NcodConcept> getNCoDConcepts() {
        //baseAPI = BaseAPI.Factory.create();
        Realm realm = Realm.getDefaultInstance();

        //Get data from the database
        RealmResults<NCoD> ncodConcept = realm.where(NCoD.class).findAllAsync();

        if(ncodConcept!=null && ncodConcept.size()>0) {
            RealmList<NcodConcept> newData = ncodConcept.get(0).getConcepts();
            return newData!=null ? (ArrayList<NcodConcept>) realm.copyFromRealm(newData) : null;//ncodConcept.get(0).getConcepts();
        }

        return null;
    }

    @Nullable
    public static ArrayList<HMISIndicatorConcept> getHMISConcepts() {
        Realm realm = Realm.getDefaultInstance();

        //Get data from the database
        RealmResults<HMISIndicator> hmisConcept = realm.where(HMISIndicator.class).findAll();

        if(hmisConcept!=null && hmisConcept.size() > 0) {
            RealmList<HMISIndicatorConcept> newData = hmisConcept.get(0).getConcepts();
            return newData!=null ? (ArrayList<HMISIndicatorConcept>) realm.copyFromRealm(newData) : null;//ncodConcept.get(0).getConcepts();
        }

        return null;
    }
}