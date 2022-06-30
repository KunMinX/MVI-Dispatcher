package com.kunminx.purenote.domain.event;

import com.kunminx.architecture.domain.event.Event;

/**
 * Create by KunMinX at 2022/6/16
 */
public class PageEvent extends Event<PageEvent.Param, PageEvent.Result> {
  public final static int EVENT_REFRESH_NOTE_LIST = 1;

  public PageEvent(int eventId) {
    this.eventId = eventId;
    this.param = new Param();
    this.result = new Result();
  }

  public static class Param {

  }

  public static class Result {
  }
}
