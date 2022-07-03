package com.kunminx.purenote.ui.page;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.purenote.R;
import com.kunminx.purenote.domain.event.Messages;
import com.kunminx.purenote.domain.message.PageMessenger;

public class MainActivity extends BaseActivity {

  private PageMessenger mMessenger;

  @Override
  protected void onInit() {
    mMessenger = getActivityScopeViewModel(PageMessenger.class);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onOutPut() {
    mMessenger.outPut(this, messages -> {
      switch (messages.eventId) {
        case Messages.EVENT_FINISH_ACTIVITY:
          finish();
          break;
      }
    });
  }

  @Override
  protected void onIntPut() {

  }
}