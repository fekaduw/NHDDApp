package et.gov.fmoh.nhddapp.nhddapp.service;

import et.gov.fmoh.nhddapp.nhddapp.model.ConceptVersion;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ApiService {

    @Streaming
    @GET("NCoD/{version}/export")
    Observable<Response<ResponseBody>> getNCoDExport(@Path("version") String version);

    @GET("NCoD/latest/")
    Observable<ConceptVersion> getNCoDVersion();
}
