package com.kunminx.architecture.utils;

import android.annotation.SuppressLint;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * Create by KunMinX at 2022/6/30
 */
public class TimeUtils {

  public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  public final static String YY_MM_DD = "yy-MM-dd";
  public final static String HH_MM_SS = "HH:mm:ss";

  public static String getCurrentTime(String time_format) {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat(time_format);
    Date curDate = new Date(System.currentTimeMillis());
    return formatter.format(curDate);
  }

  public static String getTime(long time, String format) {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    Date curDate = new Date(time);
    return formatter.format(curDate);
  }

  public static long getStringToDate(String dateString, String pattern) {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    Date date = null;
    try {
      date = (Date) dateFormat.parse(dateString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return Objects.requireNonNull(date).getTime();
  }
}
