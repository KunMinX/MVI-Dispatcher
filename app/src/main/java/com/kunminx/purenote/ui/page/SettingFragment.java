package com.kunminx.purenote.ui.page;

import com.kunminx.architecture.domain.dispatch.GlobalConfigs;
import com.kunminx.architecture.ui.bind.ClickProxy;
import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.architecture.ui.state.State;
import com.kunminx.purenote.BR;
import com.kunminx.purenote.R;
import com.kunminx.purenote.data.config.Key;

/**
 * Create by KunMinX at 2022/8/15
 */
public class SettingFragment extends BaseFragment {
  private SettingStates mStates;
  private ClickProxy mClickProxy;

  @Override
  protected void initViewModel() {
    mStates = getFragmentScopeViewModel(SettingStates.class);
  }

  @Override
  protected DataBindingConfig getDataBindingConfig() {
    mStates.testString.set(GlobalConfigs.getString(Key.TEST_STRING));
    mStates.testBoolean.set(GlobalConfigs.getBoolean(Key.TEST_BOOLEAN));
    return new DataBindingConfig(R.layout.fragment_settings, BR.state, mStates)
            .addBindingParam(BR.click, mClickProxy = new ClickProxy());
  }

  /**
   * TODO tip 1：
   * 通过唯一入口 input() 发消息至 "可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  @Override
  protected void onInput() {
    mClickProxy.setOnClickListener(v -> {
      if (v.getId() == R.id.btn_back) nav().navigateUp();
      else if (v.getId() == R.id.btn_sure_1)
        GlobalConfigs.put(Key.TEST_STRING, mStates.testString.get());
      else if (v.getId() == R.id.sw_value_2)
        GlobalConfigs.put(Key.TEST_BOOLEAN, mStates.testBoolean.get());
    });
  }

  /**
   * TODO tip 2：
   * 基于单一职责原则，抽取 Jetpack ViewModel "状态保存和恢复" 的能力作为 StateHolder，
   * 并使用 ObservableField 的改良版子类 State 来承担 BehaviorSubject，用作所绑定控件的 "可信数据源"，
   * 从而在收到来自 PublishSubject 的结果回推后，响应结果数据的变化，也即通知控件属性重新渲染，并为其兜住最后一次状态，
   *
   * 具体可参见《解决 MVI 实战痛点》解析
   * https://juejin.cn/post/7134594010642907149
   */
  public static class SettingStates extends StateHolder {
    public final State<String> testString = new State<>("");
    public final State<Boolean> testBoolean = new State<>(false);
  }
}
