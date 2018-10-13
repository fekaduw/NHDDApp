package et.gov.fmoh.nhddapp.nhddapp.utils;

import android.content.Context;

import java.util.List;

import et.gov.fmoh.nhddapp.nhddapp.model.ConceptVersion;
import et.gov.fmoh.nhddapp.nhddapp.model.NCoD;
import et.gov.fmoh.nhddapp.nhddapp.service.ApiClient;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class UpdateData {
    private static Disposable disposable;
    private String version;
    private SharedPref sharedPref;

    private void dispose() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private boolean isVersionLatest(Context context, ConceptVersion version, String type) {
        sharedPref = new SharedPref(context);
        if (version == null)
            return false;

        if (type.equalsIgnoreCase("ncod")) {
            if (sharedPref.getNCoDVersion() == null)
                return true;
            else if (DataTypeConverter.convertStringToDate(version.getCreated_on()).after(DataTypeConverter.convertStringToDate(sharedPref.getNCoDVersion()))) {
                return true;
            } else {
                return false;
            }
        } else if (type.equalsIgnoreCase("hmis")) {
            //todo:
        } else {
            return false;
        }

        return false;
    }

    public Observable<Boolean> getNcodData(Context context) {
        try {
            ConceptVersion version = ApiClient.getInstance().getNcodVersion().blockingFirst();

            if (isVersionLatest(context, version, "ncod")) {
                return ApiClient.getInstance().getNcodExport(version.getId()).flatMap(
                        responseBodyResponse -> {
                            return DataUtils.storeExportDataToDisk(responseBodyResponse.body(), "NCoD");
                        }).flatMap(fileName -> {
                            return DataUtils.unZipFile(fileName);
                        /*}).flatMap(ncodrepo -> {
                               DataUtils.populateData(context, version.getCreated_on());*/
                        });
            } else
                return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
