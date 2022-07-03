package com.kunminx.purenote.domain.event;

import com.kunminx.architecture.domain.event.Event;

/**
 * Create by KunMinX at 2022/6/16
 */
public class Messages extends Event<Messages.Param, Messages.Result> {
  public final static int EVENT_REFRESH_NOTE_LIST = 1;

  public Messages(int eventId) {
    this.eventId = eventId;
    this.param = new Param();
    this.result = new Result();
  }

  public static class Param {

  }

  public static class Result {
  }
}
