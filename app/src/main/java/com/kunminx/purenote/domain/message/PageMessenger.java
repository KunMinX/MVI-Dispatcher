package com.kunminx.purenote.domain.message;

import com.kunminx.architecture.domain.dispatch.TruthDispatcher;
import com.kunminx.purenote.domain.event.Messages;

/**
 * Create by KunMinX at 2022/6/14
 */
public class PageMessenger extends TruthDispatcher<Messages> {

  @Override
  public void input(Messages event) {
    super.input(event);
    getResult(event.eventId).setValue(event);
  }
}
