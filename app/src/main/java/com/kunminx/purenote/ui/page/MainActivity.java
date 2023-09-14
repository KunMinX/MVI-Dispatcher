package com.kunminx.purenote.ui.page;

import android.util.Log;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.purenote.BR;
import com.kunminx.purenote.R;
import com.kunminx.purenote.domain.intent.ComplexIntent;
import com.kunminx.purenote.domain.intent.Messages;
import com.kunminx.purenote.domain.message.PageMessenger;
import com.kunminx.purenote.domain.request.ComplexRequester;

import java.util.Objects;

public class MainActivity extends BaseActivity {
  private MainAtyStates mStates;
  private PageMessenger mMessenger;
  private ComplexRequester mComplexRequester;

  @Override
  protected void initViewModel() {
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);
    mComplexRequester = getActivityScopeViewModel(ComplexRequester.class);
  }

  @Override
  protected DataBindingConfig getDataBindingConfig() {
    return new DataBindingConfig(R.layout.activity_main, BR.state, mStates);
  }

  /**
   * TODO tip 1：
   * 通过 PublishSubject 接收数据，并在唯一出口 output{ ... } 中响应数据的变化，
   * 通过 BehaviorSubject 通知所绑定控件属性重新渲染，并为其兜住最后一次状态，
   */
  @Override
  protected void onOutput() {
    mMessenger.output(this, messages -> {
      if (Objects.equals(messages.id, Messages.FinishActivity.ID)) finish();
    });

    mComplexRequester.output(this, complexIntent -> {
      switch (complexIntent.id) {
        case ComplexIntent.Test1.ID:
          Log.i("ComplexIntent", "---1");
          break;
        case ComplexIntent.Test2.ID:
          Log.i("ComplexIntent", "---2");
          break;
        case ComplexIntent.Test3.ID:
          Log.i("ComplexIntent", "---3");
          break;
        case ComplexIntent.Test4.ID:
          ComplexIntent.Test4 test4 = (ComplexIntent.Test4) complexIntent;
          Log.i("ComplexIntent", "---4 " + test4.resultCount1);
          break;
      }
    });
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 input() 发消息至 "可信源"，由其内部统一处理业务逻辑和结果分发。
   *
   * 此处展示通过 dispatcher.input 连续发送多事件而不被覆盖
   */
  @Override
  protected void onInput() {
    mComplexRequester.input(ComplexIntent.Test1(1));
    mComplexRequester.input(ComplexIntent.Test2(2));
    mComplexRequester.input(ComplexIntent.Test2(2));
    mComplexRequester.input(ComplexIntent.Test2(2));
    mComplexRequester.input(ComplexIntent.Test3(3));
    mComplexRequester.input(ComplexIntent.Test3(3));
    mComplexRequester.input(ComplexIntent.Test3(3));
    mComplexRequester.input(ComplexIntent.Test3(3));
  }

  public static class MainAtyStates extends StateHolder {
  }
}