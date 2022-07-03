package com.kunminx.purenote.ui.page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.purenote.databinding.FragmentEditorBinding;
import com.kunminx.purenote.domain.message.PageMessenger;

/**
 * Create by KunMinX at 2022/6/30
 */
public class EditorFragment extends BaseFragment {

  private FragmentEditorBinding mBinding;
  private EditorViewModel mStates;
  private PageMessenger mMessenger;

  @Override
  protected View onInit(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
    mStates = getFragmentScopeViewModel(EditorViewModel.class);
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);

    mBinding = FragmentEditorBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override
  protected void onOutPut() {

  }

  @Override
  protected void onIntPut() {

  }

  public static class EditorViewModel extends ViewModel {

  }
}
