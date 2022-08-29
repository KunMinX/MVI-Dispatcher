package com.kunminx.purenote.domain.event;

import com.kunminx.architecture.domain.event.Event;
import com.kunminx.purenote.data.bean.Weather;
/**
 * Create by KunMinX at 2022/8/24
 */
public class Api extends Event<Api.Param, Api.Result> {
  public final static String API_KEY = "32d8017dd7b9c2954aa55496a62033c5";
  public final static String BASE_URL = "https://restapi.amap.com/v3/";

  public final static String GET_WEATHER_INFO = "weatherInfo";
  public final static String ERROR = "error";

  public final static String CITY_CODE_BEIJING = "110000";

  public final String node;

  public Api(String node) {
    super(0);
    this.node = node;
  }
  public Api(String node, Param param) {
    super(0, param);
    this.node = node;
  }
  public Api(String node, Param param, Result result) {
    super(0, param, result);
    this.node = node;
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

  public static Api getWeather(String cityCode) {
    return new Api(GET_WEATHER_INFO, new Param(cityCode));
  }

  public Api copy(Result result) {
    return new Api(node, param, result);
  }

  public static Api onError(Api event, String msg) {
    return new Api(ERROR, event.param, new Result(msg));
  }
}
