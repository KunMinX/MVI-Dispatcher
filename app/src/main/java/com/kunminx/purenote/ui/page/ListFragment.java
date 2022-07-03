package com.kunminx.purenote.ui.page;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.purenote.BR;
import com.kunminx.purenote.R;
import com.kunminx.purenote.domain.event.NoteListEvent;
import com.kunminx.purenote.domain.request.NoteRequester1;

/**
 * Create by KunMinX at 2022/6/30
 */
public class ListFragment extends BaseFragment {

  private ListViewModel mStates;
  private NoteRequester1 mNoteRequester;

  @Override
  protected void initViewModel() {
    mStates = getFragmentScopeViewModel(ListViewModel.class);
    mNoteRequester = getFragmentScopeViewModel(NoteRequester1.class);
  }

  @Override
  protected DataBindingConfig getDataBindingConfig() {
    return new DataBindingConfig(R.layout.fragment_list, BR.vm, mStates);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mNoteRequester.outPut(getViewLifecycleOwner(), noteListEvent -> {
      switch (noteListEvent.eventId) {
        case NoteListEvent.EVENT_GET_NOTE_LIST:
          break;
        case NoteListEvent.EVENT_MARK_ITEM:
          break;
        case NoteListEvent.EVENT_TOPPING_ITEM:
          break;
        case NoteListEvent.EVENT_REMOVE_ITEM:
          break;
      }
    });


  }

  public static class ListViewModel extends ViewModel {

  }
}
