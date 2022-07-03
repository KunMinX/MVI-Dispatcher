package com.kunminx.purenote.ui.page;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.purenote.App;
import com.kunminx.purenote.R;
import com.kunminx.purenote.domain.event.Messages;
import com.kunminx.purenote.domain.message.PageMessenger;

public class MainActivity extends BaseActivity {

  private PageMessenger mMessenger;

  @Override
  protected void onInitViewModel() {
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);
  }

  @Override
  protected void onInitView() {
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onOutput() {
    mMessenger.output(this, messages -> {
      switch (messages.eventId) {
        case Messages.EVENT_FINISH_ACTIVITY:
          finish();
          break;
      }
    });
  }

  @Override
  public void finish() {
    super.finish();
    ((App) Utils.getApp()).getViewModelStore().clear();
  }
}