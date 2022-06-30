package com.kunminx.purenote.ui.page;

import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.purenote.BR;
import com.kunminx.purenote.R;

/**
 * Create by KunMinX at 2022/6/30
 */
public class ListFragment extends BaseFragment {

  private ListViewModel mStates;

  @Override
  protected void initViewModel() {
    mStates = getFragmentScopeViewModel(ListViewModel.class);
  }

  @Override
  protected DataBindingConfig getDataBindingConfig() {
    return new DataBindingConfig(R.layout.fragment_list, BR.vm, mStates);
  }

  public static class ListViewModel extends ViewModel {

  }
}
