package com.kunminx.architecture.domain.dispatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.kunminx.architecture.domain.event.KeyValueEvent;

/**
 * Create by KunMinX at 2022/8/15
 */
public class GlobalConfigs {
  private static final KeyValueDispatcher instance = new KeyValueDispatcher();

  private GlobalConfigs() {
  }

  public static void updateUI(@NonNull AppCompatActivity activity, @NonNull Observer<KeyValueEvent> observer) {
    instance.output(activity, observer);
  }

  public static void updateUI(@NonNull Fragment fragment, @NonNull Observer<KeyValueEvent> observer) {
    instance.output(fragment, observer);
  }

  public static void put(String key, Object value) {
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
