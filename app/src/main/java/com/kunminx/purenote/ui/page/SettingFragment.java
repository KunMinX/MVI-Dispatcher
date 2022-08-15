package com.kunminx.purenote.ui.page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.architecture.domain.dispatch.GlobalConfigs;
import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.purenote.data.config.ConfigKey;
import com.kunminx.purenote.databinding.FragmentSettingsBinding;

import java.util.Objects;

/**
 * Create by KunMinX at 2022/8/15
 */
public class SettingFragment extends BaseFragment {

  private FragmentSettingsBinding mBinding;

  @Override
  protected void onInitViewModel() {
  }

  @Override
  protected View onInitView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
    mBinding = FragmentSettingsBinding.inflate(inflater, container, false);

    String s = GlobalConfigs.getString(ConfigKey.TEST_STRING);
    boolean b = GlobalConfigs.getBoolean(ConfigKey.TEST_BOOLEAN);

    mBinding.etValue1.setText(s);
    mBinding.swValue2.setChecked(b);

    return mBinding.getRoot();
  }

  @Override
  protected void onInput() {
    mBinding.btnSure1.setOnClickListener(v -> {
      String value = Objects.requireNonNull(mBinding.etValue1.getText()).toString();
      GlobalConfigs.put(ConfigKey.TEST_STRING, value);
    });
    mBinding.swValue2.setOnCheckedChangeListener((compoundButton, b) -> {
      GlobalConfigs.put(ConfigKey.TEST_BOOLEAN, b);
    });
  }

  @Override
  protected boolean onBackPressed() {
    return nav().navigateUp();
  }
}
