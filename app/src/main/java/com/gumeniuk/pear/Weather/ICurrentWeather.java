package com.gumeniuk.pear.Weather;

import com.gumeniuk.pear.Weather.model.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ICurrentWeather {

    @GET("/v1/current.json")
    Call<Weather> getData(@Query("key") String resourceName, @Query("q") String count);
}
