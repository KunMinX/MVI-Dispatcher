package com.kunminx.purenote.domain.event;

import com.kunminx.architecture.domain.event.Event;
import com.kunminx.purenote.data.bean.Weather;
/**
 * Create by KunMinX at 2022/8/24
 */
public class ApiEvent extends Event<ApiEvent.Param, ApiEvent.Result> {
  public final static String API_KEY = "32d8017dd7b9c2954aa55496a62033c5";
  public final static String BASE_URL = "https://restapi.amap.com/v3/";

  public final static String GET_WEATHER_INFO = "weatherInfo";
  public final static String ERROR = "error";

  public final static String CITY_CODE_BEIJING = "110000";

  public String api;

  public ApiEvent(String api) {
    this.api = api;
    this.param = new Param();
    this.result = new Result();
  }

  public static class Param {
    public String cityCode;
  }

  public static class Result {
    public Weather.Live live;
    public String errorInfo;
  }

  public static ApiEvent getWeather(String cityCode) {
    ApiEvent apiEvent = new ApiEvent(GET_WEATHER_INFO);
    apiEvent.param.cityCode = cityCode;
    return apiEvent;
  }

  public static ApiEvent onError(String msg) {
    ApiEvent apiEvent = new ApiEvent(ERROR);
    apiEvent.result.errorInfo = msg;
    return apiEvent;
  }
}
