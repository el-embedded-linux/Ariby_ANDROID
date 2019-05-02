package com.el.ariby.ui.api;

import com.el.ariby.ui.api.response.MeasureRepoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MeasureApi {
    String BASEURL = "http://openapi.airkorea.or.kr/";
    @GET("openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList")
    Call<MeasureRepoResponse> getMeasure (@Query("serviceKey") String key,
                                          @Query("tmX") String tmX,
                                          @Query("tmY") String tmY,
                                          @Query("_returnType") String _returnType);
}
