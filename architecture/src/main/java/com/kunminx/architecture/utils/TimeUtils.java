package com.kunminx.architecture.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Create by KunMinX at 2022/6/30
 */
public class TimeUtils {

  public final static String yyyyMMddHHmmss = "yyyy.MM.dd HH:mm:ss";
  public final static String yyyyMMddHHmmss1 = "yyyy-MM-dd HH:mm:ss";
  public final static String yyyyMMdd = "yyyy.MM.dd";
  public final static String yyMMdd = "yy-MM-dd";
  public final static String HHmmss = "HH:mm:ss";

  public static String getCurrentTime(String time_format) {
    SimpleDateFormat formatter = new SimpleDateFormat(time_format);
    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
    String time = formatter.format(curDate);
    return time;
  }

  public static String getTime(long time, String format) {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    Date curDate = new Date(time);// 获取当前时间
    String time1 = formatter.format(curDate);
    return time1;
  }

  public static long getStringToDate(String dateString, String pattern) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    Date date = null;
    try {
      date = (Date) dateFormat.parse(dateString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date.getTime();
  }
}
