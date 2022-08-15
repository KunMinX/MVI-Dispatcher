package com.kunminx.purenote.ui.page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.architecture.domain.dispatch.GlobalConfigs;
import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.purenote.R;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.data.config.ConfigKey;
import com.kunminx.purenote.databinding.FragmentListBinding;
import com.kunminx.purenote.domain.event.Messages;
import com.kunminx.purenote.domain.event.NoteEvent;
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
   *  ~
   *  Through the only exit 'dispatcher.output()' uniformly receives the message pushed back
   *  by the Single Source of Truth, and processes the UI logic according to the ID shunting.
   */
  @Override
  protected void onOutput() {
    mMessenger.output(this, messages -> {
      if (messages.eventId == Messages.EVENT_REFRESH_NOTE_LIST) {
        mNoteRequester.input(new NoteEvent(NoteEvent.EVENT_GET_NOTE_LIST));
      }
    });

    mNoteRequester.output(this, noteEvent -> {
      switch (noteEvent.eventId) {
        case NoteEvent.EVENT_TOPPING_ITEM:
        case NoteEvent.EVENT_GET_NOTE_LIST:
          mStates.list = noteEvent.result.notes;
          mAdapter.setData(mStates.list);
          mBinding.ivEmpty.setVisibility(mStates.list.size() == 0 ? View.VISIBLE : View.GONE);
          break;
        case NoteEvent.EVENT_MARK_ITEM:
          break;
        case NoteEvent.EVENT_REMOVE_ITEM:
          break;
      }
    });

    //TODO tip 3: 更新配置并刷新界面，是日常开发高频操作，
    // 当别处通过 keyValue-Dispatcher 为某配置 put 新值，此处响应并刷新 UI

    GlobalConfigs.updateUI(this, keyValueEvent -> {
      switch (keyValueEvent.currentKey) {
        case ConfigKey.TEST_STRING:
          break;
        case ConfigKey.TEST_BOOLEAN:
          break;
      }
    });
  }

  /**
   * TODO tip 2：
   *  通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   *  ~
   *  Through the unique entry 'dispatcher Input' sends a message to the Single Source of Truth,
   *  which processes the business logic and distributes the results internally.
   */
  @Override
  protected void onInput() {
    mAdapter.setListener((viewId, position, item) -> {
      if (viewId == R.id.btn_mark) {
        mNoteRequester.input(new NoteEvent(NoteEvent.EVENT_MARK_ITEM).setNote(item));
      } else if (viewId == R.id.btn_topping) {
        mNoteRequester.input(new NoteEvent(NoteEvent.EVENT_TOPPING_ITEM).setNote(item));
      } else if (viewId == R.id.btn_delete) {
        mNoteRequester.input(new NoteEvent(NoteEvent.EVENT_REMOVE_ITEM).setNote(item));
      } else if (viewId == R.id.cl) {
        EditorFragment.start(nav(), item);
      }
    });
    mBinding.fab.setOnClickListener(v -> EditorFragment.start(nav(), new Note()));
    mBinding.ivSearch.setOnClickListener(v -> nav().navigate(R.id.action_ListFragment_to_settingFragment));
    mNoteRequester.input(new NoteEvent(NoteEvent.EVENT_GET_NOTE_LIST));
  }

  @Override
  protected boolean onBackPressed() {
    mMessenger.input(new Messages(Messages.EVENT_FINISH_ACTIVITY));
    return super.onBackPressed();
  }

  public static class ListViewModel extends StateHolder {
    public List<Note> list = new ArrayList<>();
  }
}
