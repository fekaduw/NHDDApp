package et.gov.fmoh.nhddapp.nhddapp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import et.gov.fmoh.nhddapp.nhddapp.model.ConceptVersion;
import et.gov.fmoh.nhddapp.nhddapp.model.NCoD;
import et.gov.fmoh.nhddapp.nhddapp.model.NcodConcept;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface BaseAPI {
    final String BASE_API ="https://api.openconceptlab.org/orgs/EthiopiaNHDD/sources/"; //production server
    //final String BASE_API ="https://api.staging.openconceptlab.org/orgs/EthiopiaNHDD/sources/"; //staging server

    /*@GET("NCoD/concepts/?verbose=true&includeMappings=true&limit=100")
    Call<List<NcodConcept>> getNcodConcepts();*/

    @Streaming
    @GET("NCoD/{version}/export")
    Call<ResponseBody> getNCoDExport(@Path("version") String version);

    @Streaming
    @GET("HMIS-Indicators/{version}/export")
    Call<ResponseBody> getHMISIndicatorsExport(@Path("version") String version);

/*
    @GET("NCoD/latest/")
    Call<ConceptVersion> getLatestVersion();
*/

    @GET("NCoD/latest/")
    Call<ConceptVersion> getNCoDLatestVersion();

    @GET("HMIS-Indicators/latest/") //HMIS-Indicators on the production server
    Call<ConceptVersion> getHMISIndicatorLatestVersion();


    class Factory {
        public static BaseAPI create() {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient client = new OkHttpClient();

            Retrofit builder = new Retrofit.Builder()
                    .baseUrl(BASE_API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            return builder.create(BaseAPI.class);
        }
    }
}
