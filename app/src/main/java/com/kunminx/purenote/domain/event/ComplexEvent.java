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
    super(eventId);
  }
  public ComplexEvent(int eventId, Param param) {
    super(eventId, param);
  }
  public ComplexEvent(int eventId, Param param, Result result) {
    super(eventId, param, result);
  }

  public static class Param {
    public final long count;
    public Param(long count) {
      this.count = count;
    }
  }

  public static class Result {
    public final long count;
    public Result(long count) {
      this.count = count;
    }
  }

  public static ComplexEvent copy(ComplexEvent event, Result result) {
    return new ComplexEvent(event.eventId, event.param, result);
  }
}
