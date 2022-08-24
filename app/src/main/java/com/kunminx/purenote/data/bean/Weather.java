package com.kunminx.purenote.data.bean;

import java.util.List;
/**
 * Create by KunMinX at 2022/8/24
 */
public class Weather {
  private String status;
  private String count;
  private String info;
  private String infocode;
  private List<Live> lives;

  public String getStatus() {
    return status;
  }
  public String getCount() {
    return count;
  }
  public String getInfo() {
    return info;
  }
  public String getInfocode() {
    return infocode;
  }
  public List<Live> getLives() {
    return lives;
  }

  public static class Live {
    private String city;
    private String weather;
    private String temperature;

    public String getCity() {
      return city;
    }
    public String getWeather() {
      return weather;
    }
    public String getTemperature() {
      return temperature;
    }
  }
}
