package com.kunminx.architecture.domain.event;

/**
 * Create by KunMinX at 2022/8/15
 */
public class KeyValueMsg {
  public final String currentKey;

  public KeyValueMsg(String currentKey) {
    this.currentKey = currentKey;
  }
}
