package com.el.ariby.ui.api;

import com.el.ariby.ui.api.data.RepoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    String BASEURL = "http://openapi.airkorea.or.kr/";

    @GET("openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst")
    Call<RepoResponse> getWeather(@Query("serviceKey") String serviceKey,
                                  @Query("numOfRows") int numOfRows,
                                  @Query("pageNo") int pageNo,
                                  @Query("sidoName") String sidoName,
                                  @Query("searchCondition") String searchCondition,
                                  @Query("_returnType") String _returnType);
}
