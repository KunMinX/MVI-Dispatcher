package com.kunminx.purenote.domain.event;

import com.kunminx.architecture.domain.event.Event;

/**
 * Create by KunMinX at 2022/6/16
 */
public class ComplexEvent extends Event<ComplexEvent.Param, ComplexEvent.Result> {
  public final static int EVENT_TEST_1 = 1;
  public final static int EVENT_TEST_2 = 2;
  public final static int EVENT_TEST_3 = 3;
  public final static int EVENT_TEST_4 = 4;

  public ComplexEvent(int eventId) {
    this.eventId = eventId;
    this.param = new Param();
    this.result = new Result();
  }

  public static class Param {
    public long count;
  }

  public static class Result {
    public long count;
  }
}
