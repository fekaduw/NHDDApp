package et.gov.fmoh.nhddapp.nhddapp.service;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import et.gov.fmoh.nhddapp.nhddapp.model.ConceptVersion;
import et.gov.fmoh.nhddapp.nhddapp.utils.CONST;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    final String BASE_API = "https://api.openconceptlab.org/orgs/EthiopiaNHDD/sources/";

    private static ApiClient instance;
    private ApiService apiService;

    private ApiClient() {
        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public Observable<ConceptVersion> getNcodVersion() {
        Log.d(CONST.TAG, "getNcodVersion() called...");
        return apiService.getNCoDVersion();
    }

    public Observable<Response<ResponseBody>> getNcodExport(String version) {
        Log.d(CONST.TAG, "getNcodExport() called...");
        return apiService.getNCoDExport(version);
    }
}
