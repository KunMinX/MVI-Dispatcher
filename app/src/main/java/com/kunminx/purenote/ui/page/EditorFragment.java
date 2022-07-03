package com.kunminx.purenote.ui.page;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.purenote.R;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.databinding.FragmentEditorBinding;
import com.kunminx.purenote.domain.event.Messages;
import com.kunminx.purenote.domain.event.NoteListEvent;
import com.kunminx.purenote.domain.message.PageMessenger;
import com.kunminx.purenote.domain.request.NoteRequester;

import java.util.Objects;
import java.util.UUID;

/**
 * Create by KunMinX at 2022/6/30
 */
public class EditorFragment extends BaseFragment {

  private final static String NOTE = "NOTE";
  private FragmentEditorBinding mBinding;
  private EditorViewModel mStates;
  private NoteRequester mNoteRequester;
  private PageMessenger mMessenger;

  public static void start(NavController controller, Note note) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(NOTE, note);
    controller.navigate(R.id.action_ListFragment_to_EditorFragment);
  }

  @Override
  protected View onInit(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
    mStates = getFragmentScopeViewModel(EditorViewModel.class);
    mNoteRequester = getFragmentScopeViewModel(NoteRequester.class);
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);

    if (getArguments() != null) {
      mStates.originNote = getArguments().getParcelable(NOTE);
      mStates.tempNote = mStates.originNote.clone();
    }

    mBinding = FragmentEditorBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override
  protected void onOutPut() {
    mNoteRequester.outPut(getViewLifecycleOwner(), noteListEvent -> {
      switch (noteListEvent.eventId) {
        case NoteListEvent.EVENT_ADD_ITEM:
          mMessenger.input(new Messages(Messages.EVENT_REFRESH_NOTE_LIST));
          nav().navigateUp();
          break;
      }
    });
  }

  @Override
  protected void onIntPut() {
    mBinding.toolbar.setNavigationOnClickListener(v -> {
      save();
    });
  }

  private void save() {
    mStates.tempNote.title = Objects.requireNonNull(mBinding.etTitle.getText()).toString();
    mStates.tempNote.content = Objects.requireNonNull(mBinding.etContent.getText()).toString();

    if (mStates.originNote.equals(mStates.tempNote)) {
      nav().navigateUp();
      return;
    }
    long time = System.currentTimeMillis();
    if (TextUtils.isEmpty(mStates.originNote.id)) {
      mStates.tempNote.createTime = time;
      mStates.tempNote.id = UUID.randomUUID().toString();
    }
    mStates.tempNote.modifyTime = time;

    NoteListEvent event = new NoteListEvent(NoteListEvent.EVENT_ADD_ITEM);
    event.param.note = mStates.tempNote;
    mNoteRequester.input(event);
  }

  @Override
  protected boolean onBackPressed() {
    save();
    return true;
  }

  public static class EditorViewModel extends ViewModel {
    public Note originNote = new Note();
    public Note tempNote = new Note();
  }
}
