package com.kunminx.purenote.domain.message;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.purenote.domain.event.Messages;

/**
 * Create by KunMinX at 2022/6/14
 */
public class PageMessenger extends MviDispatcher<Messages> {

  @Override
  public void input(Messages event) {
    sendResult(event);

// TODO：tip 1：除接收 Activity/Fragment 事件，亦可从 Dispatcher 内部发送事件（作为副作用）：
//  if (此处欲内部推送) {
//    Messages msg = new Messages(Messages.EVENT_SHOW_DIALOG);
//    sendResult(msg);
//  }

  }
}

