package com.kunminx.architecture.domain.event;

/**
 * Create by KunMinX at 2022/6/16
 */
public class Event<P, R> {
  public final int eventId;
  public final P param;
  public final R result;

  public Event(int eventId) {
    this.eventId = eventId;
    this.param = null;
    this.result = null;
  }

  public Event(int eventId, P param) {
    this.eventId = eventId;
    this.param = param;
    this.result = null;
  }

  public Event(int eventId, P param, R result) {
    this.eventId = eventId;
    this.param = param;
    this.result = result;
  }
}
