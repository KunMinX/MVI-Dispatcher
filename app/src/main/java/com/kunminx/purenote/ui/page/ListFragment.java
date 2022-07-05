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
  protected void onInitViewModel() {
    mStates = getFragmentScopeViewModel(ListViewModel.class);
    mNoteRequester = getFragmentScopeViewModel(NoteRequester.class);
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);
  }

  @Override
  protected View onInitView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
    mBinding = FragmentListBinding.inflate(inflater, container, false);
    mBinding.rv.setAdapter(mAdapter = new NoteAdapter());
    return mBinding.getRoot();
  }

  /**
   * TODO tip 1：
   *  通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  @Override
  protected void onOutput() {
    mMessenger.output(this, messages -> {
      if (messages.eventId == Messages.EVENT_REFRESH_NOTE_LIST) {
        mNoteRequester.input(new NoteListEvent(NoteListEvent.EVENT_GET_NOTE_LIST));
      }
    });

    mNoteRequester.output(this, noteListEvent -> {
      switch (noteListEvent.eventId) {
        case NoteListEvent.EVENT_TOPPING_ITEM:
        case NoteListEvent.EVENT_GET_NOTE_LIST:
          mStates.list = noteListEvent.result.notes;
          mAdapter.setData(mStates.list);
          mBinding.ivEmpty.setVisibility(mStates.list.size() == 0 ? View.VISIBLE : View.GONE);
          break;
        case NoteListEvent.EVENT_MARK_ITEM:
          break;
        case NoteListEvent.EVENT_REMOVE_ITEM:
          break;
      }
    });
  }

  /**
   * TODO tip 2：
   *  通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  @Override
  protected void onInput() {
    mAdapter.setListener((viewId, position, item) -> {
      if (viewId == R.id.btn_mark) {
        mNoteRequester.input(new NoteListEvent(NoteListEvent.EVENT_MARK_ITEM).setNote(item));
      } else if (viewId == R.id.btn_topping) {
        mNoteRequester.input(new NoteListEvent(NoteListEvent.EVENT_TOPPING_ITEM).setNote(item));
      } else if (viewId == R.id.btn_delete) {
        mNoteRequester.input(new NoteListEvent(NoteListEvent.EVENT_REMOVE_ITEM).setNote(item));
      } else if (viewId == R.id.cl) {
        EditorFragment.start(nav(), item);
      }
    });

    mBinding.fab.setOnClickListener(v -> EditorFragment.start(nav(), new Note()));

    mNoteRequester.input(new NoteListEvent(NoteListEvent.EVENT_GET_NOTE_LIST));
  }

  @Override
  protected boolean onBackPressed() {
    mMessenger.input(new Messages(Messages.EVENT_FINISH_ACTIVITY));
    return super.onBackPressed();
  }

  public static class ListViewModel extends ViewModel {
    public List<Note> list = new ArrayList<>();
  }
}
