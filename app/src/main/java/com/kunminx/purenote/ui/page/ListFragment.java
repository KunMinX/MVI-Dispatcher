package com.kunminx.purenote.ui.page;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.page.State;
import com.kunminx.purenote.BR;
import com.kunminx.purenote.R;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.domain.event.NoteListEvent;
import com.kunminx.purenote.domain.request.NoteRequester;
import com.kunminx.purenote.ui.adapter.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by KunMinX at 2022/6/30
 */
public class ListFragment extends BaseFragment {

  private ListViewModel mStates;
  private NoteRequester mNoteRequester;

  @Override
  protected void initViewModel() {
    mStates = getFragmentScopeViewModel(ListViewModel.class);
    mNoteRequester = getFragmentScopeViewModel(NoteRequester.class);
  }

  @Override
  protected DataBindingConfig getDataBindingConfig() {
    NoteAdapter adapter = new NoteAdapter(mActivity.getApplicationContext());
    adapter.setOnItemClickListener((viewId, item, position) -> {
      if (viewId == R.id.btn_mark) {
        item.toggleType(Note.TYPE_MARKED);
      } else if (viewId == R.id.btn_topping) {
        item.toggleType(Note.TYPE_TOPPING);
      } else if (viewId == R.id.tv_title) {

      }
    });
    return new DataBindingConfig(R.layout.fragment_list, BR.vm, mStates)
            .addBindingParam(BR.adapter, adapter);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mNoteRequester.outPut(getViewLifecycleOwner(), noteListEvent -> {
      switch (noteListEvent.eventId) {
        case NoteListEvent.EVENT_GET_NOTE_LIST:
          mStates.list.set(noteListEvent.result.notes);
          break;
        case NoteListEvent.EVENT_MARK_ITEM:
          break;
        case NoteListEvent.EVENT_TOPPING_ITEM:
          break;
        case NoteListEvent.EVENT_REMOVE_ITEM:
          break;
      }
    });

    mNoteRequester.input(new NoteListEvent(NoteListEvent.EVENT_GET_NOTE_LIST));
  }

  public static class ListViewModel extends ViewModel {
    public State<List<Note>> list = new State<>(new ArrayList<>());
  }
}
