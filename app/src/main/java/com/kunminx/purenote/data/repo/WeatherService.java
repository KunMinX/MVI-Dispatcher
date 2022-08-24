package com.kunminx.purenote.data.repo;

import com.kunminx.purenote.data.bean.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
/**
 * Create by KunMinX at 2022/8/24
 */
public interface WeatherService {
  @GET("weather/{api}")
  Call<Weather> getWeatherInfo(
          @Path("api") String api,
          @Query("city") String city,
          @Query("key") String key
  );
}