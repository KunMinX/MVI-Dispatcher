package com.kunminx.architecture.domain.event;

/**
 * Create by KunMinX at 2022/6/16
 */
public class Event<P, R> {
  public int eventId;
  public P param;
  public R result;
}
