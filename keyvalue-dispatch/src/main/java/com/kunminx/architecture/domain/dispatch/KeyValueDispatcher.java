package com.kunminx.architecture.domain.dispatch;

import com.kunminx.architecture.domain.event.KeyValueEvent;
import com.kunminx.architecture.utils.SPUtils;

import java.util.HashMap;

/**
 * Create by KunMinX at 2022/8/15
 */
public class KeyValueDispatcher extends MviDispatcher<KeyValueEvent> {
  private final HashMap<String, Object> keyValues = new HashMap<>();
  private final SPUtils mSPUtils = SPUtils.getInstance(moduleName());

  public String moduleName() {
    return "GlobalConfigs";
  }

  @Override
  protected void onHandle(KeyValueEvent event) {
    sendResult(event);
  }

  public void put(String key, String value) {
    keyValues.put(key, value);
    mSPUtils.put(key, value);
    input(new KeyValueEvent(key));
  }

  public void put(String key, Integer value) {
    keyValues.put(key, value);
    mSPUtils.put(key, value);
    input(new KeyValueEvent(key));
  }

  public void put(String key, Long value) {
    keyValues.put(key, value);
    mSPUtils.put(key, value);
    input(new KeyValueEvent(key));
  }

  public void put(String key, Float value) {
    keyValues.put(key, value);
    mSPUtils.put(key, value);
    input(new KeyValueEvent(key));
  }

  public void put(String key, Boolean value) {
    keyValues.put(key, value);
    mSPUtils.put(key, value);
    input(new KeyValueEvent(key));
  }

  public String getString(String key) {
    if (keyValues.get(key) == null) {
      keyValues.put(key, mSPUtils.getString(key, ""));
    }
    return (String) keyValues.get(key);
  }

  public Integer getInt(String key) {
    if (keyValues.get(key) == null) {
      keyValues.put(key, mSPUtils.getInt(key, 0));
    }
    return (Integer) keyValues.get(key);
  }

  public Long getLong(String key) {
    if (keyValues.get(key) == null) {
      keyValues.put(key, mSPUtils.getLong(key, 0));
    }
    return (Long) keyValues.get(key);
  }

  public Float getFloat(String key) {
    if (keyValues.get(key) == null) {
      keyValues.put(key, mSPUtils.getFloat(key, 0));
    }
    return (Float) keyValues.get(key);
  }

  public Boolean getBoolean(String key) {
    if (keyValues.get(key) == null) {
      keyValues.put(key, mSPUtils.getBoolean(key, false));
    }
    return (Boolean) keyValues.get(key);
  }
}
