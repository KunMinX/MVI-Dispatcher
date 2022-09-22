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

  @Override
  protected boolean onBackPressed() {
    return nav().navigateUp();
  }

  /**
   * TODO tip 3：传统 MVI 属于对响应式编程的填坑和升级，
   *  故通常是两层架构：表现层和数据层，
   *  ViewModel 是表现层组件，业务逻辑状态改变都在 ViewModel 中写，
   *  响应式编程便于单元测试和关注点分离，同时也使同质化的业务逻辑分散在多个 ViewModel 中，易造成修改时的不一致，
   *  ~
   *  故综合考虑，本项目示例采用三层架构，即 表现层、领域层、数据层，
   *  StateHolder 属于表现层，为页面专属；MVI-Dispatcher 属于领域层，可供同业务不同页面复用，
   *  领域层组件通过 PublishSubject（例如 SharedFlow）分发结果至表现层，
   *  对于状态，由 BehaviorSubject（例如以下 State 组件）响应和兜着；对于事件，则一次性执行，
   *
   * 具体可参见《解决 MVI 实战痛点》解析
   * https://juejin.cn/post/7134594010642907149
   */
  public static class SettingStates extends StateHolder {
    public final State<String> testString = new State<>("");
    public final State<Boolean> testBoolean = new State<>(false);
  }
}
