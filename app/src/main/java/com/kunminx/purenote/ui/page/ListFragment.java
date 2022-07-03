package com.kunminx.purenote.ui.page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.purenote.R;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.databinding.FragmentListBinding;
import com.kunminx.purenote.domain.event.Messages;
import com.kunminx.purenote.domain.event.NoteListEvent;
import com.kunminx.purenote.domain.message.PageMessenger;
import com.kunminx.purenote.domain.request.NoteRequester;
import com.kunminx.purenote.ui.adapter.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by KunMinX at 2022/6/30
 */
public class ListFragment extends BaseFragment {

  private FragmentListBinding mBinding;
  private ListViewModel mStates;
  private NoteRequester mNoteRequester;
  private PageMessenger mMessenger;
  private NoteAdapter mAdapter;

  @Override
  protected View onInit(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
    mStates = getFragmentScopeViewModel(ListViewModel.class);
    mNoteRequester = getFragmentScopeViewModel(NoteRequester.class);
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);
    mAdapter = new NoteAdapter();
    mBinding = FragmentListBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override
  protected void onOutPut() {
    mMessenger.outPut(getViewLifecycleOwner(), messages -> {
      switch (messages.eventId) {
        case Messages.EVENT_REFRESH_NOTE_LIST:
          mNoteRequester.input(new NoteListEvent(NoteListEvent.EVENT_GET_NOTE_LIST));
          break;
      }
    });

    mNoteRequester.outPut(getViewLifecycleOwner(), noteListEvent -> {
      switch (noteListEvent.eventId) {
        case NoteListEvent.EVENT_GET_NOTE_LIST:
          mStates.list = noteListEvent.result.notes;
          mAdapter.setData(mStates.list);
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

  @Override
  protected void onIntPut() {
    mAdapter.setListener((viewId, position, item) -> {
      if (viewId == R.id.btn_mark) {
        item.toggleType(Note.TYPE_MARKED);
      } else if (viewId == R.id.btn_topping) {
        item.toggleType(Note.TYPE_TOPPING);
      } else if (viewId == R.id.tv_title) {
        EditorFragment.start(nav(), item);
      }
    });

    mBinding.fab.setOnClickListener(v -> {
      EditorFragment.start(nav(), new Note());
    });

    mNoteRequester.input(new NoteListEvent(NoteListEvent.EVENT_GET_NOTE_LIST));
  }

  public static class ListViewModel extends ViewModel {
    public List<Note> list = new ArrayList<>();
  }
}
