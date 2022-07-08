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
import com.kunminx.architecture.utils.ToastUtils;
import com.kunminx.purenote.R;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.databinding.FragmentEditorBinding;
import com.kunminx.purenote.domain.event.Messages;
import com.kunminx.purenote.domain.event.NoteEvent;
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
    controller.navigate(R.id.action_ListFragment_to_EditorFragment, bundle);
  }

  @Override
  protected void onInitViewModel() {
    mStates = getFragmentScopeViewModel(EditorViewModel.class);
    mNoteRequester = getFragmentScopeViewModel(NoteRequester.class);
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);
  }

  @Override
  protected View onInitView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
    mBinding = FragmentEditorBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override
  protected void onInitData() {
    if (getArguments() != null) {
      mStates.tempNote = getArguments().getParcelable(NOTE);
      mStates.title = mStates.tempNote.title;
      mStates.content = mStates.tempNote.content;
      if (!TextUtils.isEmpty(mStates.tempNote.id)) {
        mBinding.etTitle.setText(mStates.tempNote.title);
        mBinding.etContent.setText(mStates.tempNote.content);
        mBinding.tvTitle.setText(getString(R.string.last_time_modify));
        mBinding.tvTime.setText(mStates.tempNote.getModifyDate());
      }
    }
  }

  /**
   * TODO tip 1：
   *  通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  @Override
  protected void onOutput() {
    mNoteRequester.output(this, noteEvent -> {
      if (noteEvent.eventId == NoteEvent.EVENT_ADD_ITEM) {
        mMessenger.input(new Messages(Messages.EVENT_REFRESH_NOTE_LIST));
        ToastUtils.showShortToast(getString(R.string.saved));
        nav().navigateUp();
      }
    });
  }

  /**
   * TODO tip 2：
   *  通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  @Override
  protected void onInput() {
    mBinding.btnBack.setOnClickListener(v -> save());
  }

  private void save() {
    mStates.tempNote.title = Objects.requireNonNull(mBinding.etTitle.getText()).toString();
    mStates.tempNote.content = Objects.requireNonNull(mBinding.etContent.getText()).toString();
    if (TextUtils.isEmpty(mStates.tempNote.title) && TextUtils.isEmpty(mStates.tempNote.content)
            || mStates.tempNote.title.equals(mStates.title) && mStates.tempNote.content.equals(mStates.content)) {
      nav().navigateUp();
      return;
    }
    long time = System.currentTimeMillis();
    if (TextUtils.isEmpty(mStates.tempNote.id)) {
      mStates.tempNote.createTime = time;
      mStates.tempNote.id = UUID.randomUUID().toString();
    }
    mStates.tempNote.modifyTime = time;

    mNoteRequester.input(new NoteEvent(NoteEvent.EVENT_ADD_ITEM).setNote(mStates.tempNote));
  }

  @Override
  protected boolean onBackPressed() {
    save();
    return super.onBackPressed();
  }

  public static class EditorViewModel extends ViewModel {
    public Note tempNote = new Note();
    public String title = "";
    public String content = "";
  }
}
