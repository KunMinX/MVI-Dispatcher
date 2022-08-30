package com.kunminx.architecture.domain.dispatch;

import com.kunminx.architecture.domain.event.KeyValueCallback;
import com.kunminx.architecture.utils.SPUtils;

import java.util.HashMap;

/**
 * TODO tip：通过内聚，消除 Key Value Getter Putter init 样板代码，
 *  开发者只需声明 Key 列表即可使用 get() put()，
 *  且顺带可从唯一出口 output 处响应配置变化完成 UI 刷新
 *
 * Create by KunMinX at 2022/8/15
 */
public class KeyValueDispatcher extends MviDispatcher<KeyValueCallback> {
  private final HashMap<String, Object> keyValues = new HashMap<>();
  private final SPUtils mSPUtils = SPUtils.getInstance(moduleName());

  public String moduleName() {
    return "GlobalConfigs";
  }

  @Override
  protected void onHandle(KeyValueCallback intent) {
    sendResult(intent);
  }

  public void put(String key, String value) {
    keyValues.put(key, value);
    mSPUtils.put(key, value);
    input(new KeyValueCallback(key));
  }

  public void put(String key, Integer value) {
    keyValues.put(key, value);
    mSPUtils.put(key, value);
    input(new KeyValueCallback(key));
  }

  public void put(String key, Long value) {
    keyValues.put(key, value);
    mSPUtils.put(key, value);
    input(new KeyValueCallback(key));
  }

  public void put(String key, Float value) {
    keyValues.put(key, value);
    mSPUtils.put(key, value);
    input(new KeyValueCallback(key));
  }

  public void put(String key, Boolean value) {
    keyValues.put(key, value);
    mSPUtils.put(key, value);
    input(new KeyValueCallback(key));
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
