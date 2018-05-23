package et.gov.fmoh.nhddapp.nhddapp.service;

import android.telecom.Call;

import java.util.List;

import et.gov.fmoh.nhddapp.nhddapp.model.Concept;
import retrofit2.http.GET;

public interface NhddClient {

    @GET("/concepts/")
    retrofit2.Call<List<Concept>> reposForConcept();
}
