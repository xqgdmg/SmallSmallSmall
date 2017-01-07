package com.example.administrator.myapplication.api;

import com.example.administrator.myapplication.bean.Configures;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/11/21.
 * description： Retrofit网络请求 API
 */
public interface Rest {


    /**
     * 各种配置，测试
     *
     */
    @Headers("Content-Type: application/json")
    @POST("api/v1/configures")
    Call<Configures> listConfigures();

    @GET("http://www.baidu.com")
    Call<String> GetbookOffer();

}
