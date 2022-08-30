package com.kunminx.architecture.domain.event;

/**
 * Create by KunMinX at 2022/8/15
 */
public class KeyValueCallback extends Event<KeyValueCallback.Param, KeyValueCallback.Result> {
  public final String currentKey;

  public KeyValueCallback(String currentKey) {
    super(0, new Param(), new Result());
    this.currentKey = currentKey;
  }

  public static class Param {

  }

  public static class Result {
  }
}
