package com.kunminx.purenote.domain.message;

import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.domain.message.MutableResult;
import com.kunminx.architecture.domain.message.Result;
import com.kunminx.purenote.domain.event.PageEvent;

/**
 * Create by KunMinX at 2022/6/14
 */
public class PageMessenger extends ViewModel {

  private final MutableResult<PageEvent> pageResult = new MutableResult<>();

  public Result<PageEvent> getPageResult() {
    return pageResult;
  }

  public void requestPageEvent(PageEvent event) {
    pageResult.setValue(event);
  }
}
