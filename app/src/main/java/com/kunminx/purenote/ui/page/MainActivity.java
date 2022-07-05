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

  /**
   * TODO tip 1：
   *  通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  @Override
  protected void onOutput() {
    mMessenger.output(this, messages -> {
      if (messages.eventId == Messages.EVENT_FINISH_ACTIVITY) finish();
    });
  }

  @Override
  public void finish() {
    super.finish();
    ((App) Utils.getApp()).getViewModelStore().clear();
  }
}