package com.kunminx.architecture.domain.dispatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.kunminx.architecture.domain.event.KeyValueMsg;

/**
 * TODO tip: 此处基于 KeyValueDispatcher 提供全局配置的默认实现，
 *  开发者亦可继承 KeyValueDispatcher 或效仿本类实现局部或全局用 KeyValueStore
 *
 * Create by KunMinX at 2022/8/15
 */
public class GlobalConfigs {
  private static final KeyValueDispatcher instance = new KeyValueDispatcher();

  private GlobalConfigs() {
  }

  public static void output(@NonNull AppCompatActivity activity, @NonNull Observer<KeyValueMsg> observer) {
    instance.output(activity, observer);
  }

  public static void output(@NonNull Fragment fragment, @NonNull Observer<KeyValueMsg> observer) {
    instance.output(fragment, observer);
  }

  public static void put(String key, String value) {
    instance.put(key, value);
  }

  public static void put(String key, Integer value) {
    instance.put(key, value);
  }

  public static void put(String key, Long value) {
    instance.put(key, value);
  }

  public static void put(String key, Float value) {
    instance.put(key, value);
  }

  public static void put(String key, Boolean value) {
    instance.put(key, value);
  }

  public static String getString(String key) {
    return instance.getString(key);
  }

  public static Integer getInt(String key) {
    return instance.getInt(key);
  }

  public static Long getLong(String key) {
    return instance.getLong(key);
  }

  public static Float getFloat(String key) {
    return instance.getFloat(key);
  }

  public static Boolean getBoolean(String key) {
    return instance.getBoolean(key);
  }
}
