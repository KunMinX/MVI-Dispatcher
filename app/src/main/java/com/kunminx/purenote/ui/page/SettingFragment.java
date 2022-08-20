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
    mClickProxy.setOnClick(v -> {
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

  public static class SettingStates extends StateHolder {
    public final State<String> testString = new State<>("");
    public final State<Boolean> testBoolean = new State<>(false);
  }
}
