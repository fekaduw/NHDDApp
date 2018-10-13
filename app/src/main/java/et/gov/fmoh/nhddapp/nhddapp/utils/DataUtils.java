package et.gov.fmoh.nhddapp.nhddapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.model.HMISIndicator;
import et.gov.fmoh.nhddapp.nhddapp.model.NCoD;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.realm.Realm;
import io.realm.RealmResults;
import lombok.Getter;
import lombok.Setter;
import okhttp3.ResponseBody;

import static et.gov.fmoh.nhddapp.nhddapp.utils.CONST.TAG;

public class DataUtils {
    private static String fileName;

    @NonNull
    public static Observable<String> storeExportDataToDisk(final ResponseBody body, final String dataType) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                try {
                    Log.d(TAG, "************ storeExportDataToDiskAbout() called **************");
                    String currentDate = getCurrentDate();

                    fileName = dataType.concat("_" + currentDate.replace("-", ""));
                    Log.d(TAG, "*********** FileName : " + fileName);


                    File downloadFile = new File(
                            //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"NCod.zip");

                            CONST.LOCAL_STORAGE, dataType.isEmpty() ? "unknown" : fileName + ".zip");
                    Log.d(TAG, "File location identified: " + downloadFile);

//                        if(downloadFile.exists()){
//                            //File exportedFile = new File(CONST.LOCAL_STORAGE, fileName)
//                            deleteFile(downloadFile,null);
//                        }

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

                            Log.d(TAG, "Size of file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                        }

                        outputStream.flush();

                        //return true;
                        emitter.onNext(fileName);
                        emitter.onComplete();
                    } catch (IOException e) {

                        Log.d(TAG, "File could not be downloaded/stored\n" + e.getStackTrace());

                        //return false;
                        emitter.onNext(null);
                        emitter.onComplete();
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }

                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }
                } catch (IOException e) {
                    Log.d(TAG, "File could not be downloaded/stored");

                    //return false;
                    emitter.onNext(null);
                    emitter.onComplete();
                }
            }
        });
    }

    public static void unzip(String zipFile, String location) {
        try {
            FileInputStream inputStream = new FileInputStream(zipFile);
            ZipInputStream zipStream = new ZipInputStream(inputStream);
            ZipEntry zEntry = null;
            while ((zEntry = zipStream.getNextEntry()) != null) {
                Log.d(TAG, "Unzipping " + zEntry.getName() + " at "
                        + location);

                if (zEntry.isDirectory()) {
                    String path = location + File.separator + zEntry.getName();
                    handleDirectory(path);
                } else {
                    Log.d(TAG, "****** Unzip the file in this location: " + location + "/" + fileName + "_" + zEntry.getName() + ".zip");
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
            Log.d(TAG, "Unzipping complete. path :  " + location);
        } catch (Exception e) {
            Log.d(TAG, "Unzipping failed");
            e.printStackTrace();
        }
    }

    public static void handleDirectory(String location) {
        File f = new File(location);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    public static Observable<Boolean> unZipFile(String fileName) throws IOException {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                     @Override
                                     public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {

                                         //String backupDBPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/NCod.zip";
                                         String backupDBPath = CONST.LOCAL_STORAGE + "/" + fileName + ".zip";

                                         //File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                         File sd = CONST.LOCAL_STORAGE;
                                         if (sd.canWrite()) {
                                             final File backupDBFolder = new File(sd.getPath());
                                             unzip(backupDBPath, backupDBFolder.getPath());

                                             emitter.onNext(true);
                                             emitter.onComplete();
                                         }
                                         emitter.onNext(false);
                                         emitter.onComplete();
                                     }
                                 }
        );
    }

    public static void deleteFile(File zipFile, File exportFile) {
        if (zipFile != null)
            zipFile.delete();

        if (exportFile != null)
            exportFile.delete();
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
//

    public static void populateData(Context context, String latestVersion) {
        Realm realm = Realm.getDefaultInstance();

        //transaction timer
        final TransactionTime transactionTime = new TransactionTime(System.currentTimeMillis());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String currentDate = getCurrentDate().replace("-", "");
                try {
                    InputStream inputStream = new FileInputStream(new File(CONST.LOCAL_STORAGE, "NCoD_" + currentDate + "_export.json"));
                    realm.createObjectFromJson(NCoD.class, inputStream);
                    Log.d("Realm", "Successfully added the data.");
                    transactionTime.setEnd(System.currentTimeMillis());

                    if (!realm.isClosed()) {
                        realm.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    SharedPref sharedPref = new SharedPref(context);
                    sharedPref.setNCoDVersion(latestVersion);

                    Log.d(TAG, "Processing finally{}... to delete files");

                    //todo: deleteFile(new File(CONST.LOCAL_STORAGE, "NCoD_" + currentDate + ".zip"), file);

                }
            }
        });
        Log.d("Realm", "createAllFromJson Task completed in " + transactionTime.getDuration() + "ms");
    }

    public static void importeNcodData(Context context, final Resources resources) {
        Realm realm = Realm.getDefaultInstance();

        //transaction timer
        final TransactionTime transactionTime = new TransactionTime(System.currentTimeMillis());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                InputStream inputStream = resources.openRawResource(R.raw.exportncod);
                try {
                    realm.createObjectFromJson(NCoD.class, inputStream);
                    Log.d(TAG, "Successfully added the data.");
                    transactionTime.setEnd(System.currentTimeMillis());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(!realm.isClosed()){
                        //realm.close();
                    }
                }
            }
        });
        Log.d(TAG, "createAllFromJson Task completed in " + transactionTime.getDuration() + "ms");
    }

    public static void importeHMISData(Context context, final Resources resources) {
        Realm realm = Realm.getDefaultInstance();

        //transaction timer
        final TransactionTime transactionTime = new TransactionTime(System.currentTimeMillis());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                InputStream inputStream = resources.openRawResource(R.raw.exporthmis);
                try {
                    realm.createObjectFromJson(HMISIndicator.class, inputStream);
                    Log.d(TAG, "Successfully added the HMIS data.");
                    transactionTime.setEnd(System.currentTimeMillis());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(!realm.isClosed()){
                        //realm.close();
                    }
                }
            }
        });
        Log.d(TAG, "createAllFromJson Task completed in " + transactionTime.getDuration() + "ms");
    }

    /*        //return Observable.create(emitter -> {
                Log.d(TAG, "Inside PopulateData()...");
                Realm realm = Realm.getDefaultInstance();

                //transaction timer
                final TransactionTime transactionTime = new TransactionTime(System.currentTimeMillis());

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        String currentDate = null;
                        File file = null;

                        try {
                            //realm.beginTransaction();

                            Log.d(TAG, "Is the realm object closed? " + realm.isClosed());

                            currentDate = getCurrentDate().replace("-", "");
                            file = new File(CONST.LOCAL_STORAGE, "NCoD_" + currentDate + "_export.json");
                            InputStream stream = new FileInputStream(file);
                            Log.d(TAG, " ********** fileName after unzip : " + file.toString());

                            realm.createObjectFromJson(NCoD.class, stream);

                            Log.d(TAG, "createObjectFromJson() processed!");

                            transactionTime.setEnd(System.currentTimeMillis());

                            //realm.commitTransaction();
                            Log.d(TAG, "createObjectFromJson() Task completed in " + transactionTime.getDuration() + "ms");

                 *//*           RealmResults<NCoD> concepts = null;
                        concepts = realm.where(NCoD.class).findAllAsync();
                        if (concepts != null && concepts.size() >= 0) {
                            Log.d(TAG, "Updated database from the JSON file");

                            //fetch the data from the OCL using the API and cache it on the local db
                            Log.d(TAG, "Sample data after the database was updated: " + concepts.get(0).getConcepts().get(0).getDisplay_name());

                            emitter.onNext(concepts);
                            emitter.onComplete();
                        }*//*

                        if (!realm.isClosed()) {
                            realm.close();
                        }

                        Log.d(TAG, "Is the realm object closed? " + realm.isClosed());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                   *//*     emitter.onNext(null);
                        emitter.onComplete();*//*
                    } catch (IOException e) {
                        if (realm.isInTransaction())
                            realm.cancelTransaction();
                        e.printStackTrace();
                        Log.e(TAG, "Updating the database failed.\n" + e.getMessage());
                  *//*      emitter.onNext(null);
                        emitter.onComplete();*//*
                    } finally {
                        //save the version in the sharedPref
                        SharedPref sharedPref = new SharedPref(context);
                        sharedPref.setNCoDVersion(latestVersion);

                        Log.d(TAG, "Processing finally{}... to delete files");

                        //todo: deleteFile(new File(CONST.LOCAL_STORAGE, "NCoD_" + currentDate + ".zip"), file);

                        *//*if (!realm.isClosed()) {
                            //todo   realm.close();
                        }*//*
                    }
                }
            });
        //});
    }*/

}
