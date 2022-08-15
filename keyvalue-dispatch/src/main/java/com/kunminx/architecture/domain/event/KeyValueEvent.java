package com.kunminx.architecture.domain.event;

/**
 * Create by KunMinX at 2022/8/15
 */
public class KeyValueEvent extends Event<KeyValueEvent.Param, KeyValueEvent.Result> {
  public KeyValueEvent(int eventId) {
    this.eventId = eventId;
    this.param = new Param();
    this.result = new Result();
  }

  public static class Param {

  }

  public static class Result {
  }
}
