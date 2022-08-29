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

  public final String api;

  public ApiEvent(String api) {
    super(0);
    this.api = api;
  }
  public ApiEvent(String api, Param param) {
    super(0, param);
    this.api = api;
  }
  public ApiEvent(String api, Param param, Result result) {
    super(0, param, result);
    this.api = api;
  }

  public static class Param {
    public final String cityCode;
    public Param(String cityCode) {
      this.cityCode = cityCode;
    }
  }

  public static class Result {
    public final Weather.Live live;
    public final String errorInfo;
    public Result(Weather.Live live) {
      this.live = live;
      this.errorInfo = "";
    }
    public Result(String errorInfo) {
      this.live = null;
      this.errorInfo = errorInfo;
    }
  }

  public static ApiEvent getWeather(String cityCode) {
    return new ApiEvent(GET_WEATHER_INFO, new Param(cityCode));
  }

  public static ApiEvent copy(ApiEvent event, Result result) {
    return new ApiEvent(event.api, event.param, result);
  }

  public static ApiEvent onError(ApiEvent event, String msg) {
    return new ApiEvent(ERROR, event.param, new Result(msg));
  }
}
