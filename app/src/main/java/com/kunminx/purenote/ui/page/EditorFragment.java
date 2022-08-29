package com.kunminx.purenote.ui.page;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.navigation.NavController;

import com.kunminx.architecture.ui.bind.ClickProxy;
import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.architecture.ui.state.State;
import com.kunminx.architecture.utils.ToastUtils;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.purenote.BR;
import com.kunminx.purenote.R;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.domain.event.Messages;
import com.kunminx.purenote.domain.event.NoteEvent;
import com.kunminx.purenote.domain.message.PageMessenger;
import com.kunminx.purenote.domain.request.NoteRequester;

import java.util.UUID;

/**
 * Create by KunMinX at 2022/6/30
 */
public class EditorFragment extends BaseFragment {
  private final static String NOTE = "NOTE";
  private EditorStates mStates;
  private NoteRequester mNoteRequester;
  private PageMessenger mMessenger;
  private ClickProxy mClickProxy;

  public static void start(NavController controller, Note note) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(NOTE, note);
    controller.navigate(R.id.action_list_to_editor, bundle);
  }

  @Override
  protected void initViewModel() {
    mStates = getFragmentScopeViewModel(EditorStates.class);
    mNoteRequester = getFragmentScopeViewModel(NoteRequester.class);
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);
  }

  @Override
  protected DataBindingConfig getDataBindingConfig() {
    return new DataBindingConfig(R.layout.fragment_editor, BR.state, mStates)
            .addBindingParam(BR.click, mClickProxy = new ClickProxy());
  }

  @Override
  protected void onInitData() {
    if (getArguments() != null) {
      mStates.tempNote = getArguments().getParcelable(NOTE);
      mStates.title.set(mStates.tempNote.title);
      mStates.content.set(mStates.tempNote.content);
      if (TextUtils.isEmpty(mStates.tempNote.id)) {
        mStates.titleRequestFocus.set(true);
      } else {
        mStates.tip.set(getString(R.string.last_time_modify));
        mStates.time.set(mStates.tempNote.getModifyDate());
      }
    }
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
   *  ~
   *  Through the unique entry 'dispatcher Input' sends a message to the Single Source of Truth,
   *  which processes the business logic and distributes the results internally.
   */
  @Override
  protected void onInput() {
    mClickProxy.setOnClickListener(v -> {
      if (v.getId() == R.id.btn_back) save();
    });
  }

  private boolean save() {
    if (TextUtils.isEmpty(mStates.title.get() + mStates.content.get())
            || mStates.tempNote.title.equals(mStates.title.get())
            && mStates.tempNote.content.equals(mStates.content.get())) {
      return nav().navigateUp();
    }
    mStates.tempNote.title = mStates.title.get();
    mStates.tempNote.content = mStates.content.get();
    long time = System.currentTimeMillis();
    if (TextUtils.isEmpty(mStates.tempNote.id)) {
      mStates.tempNote.createTime = time;
      mStates.tempNote.id = UUID.randomUUID().toString();
    }
    mStates.tempNote.modifyTime = time;
    mNoteRequester.input(NoteEvent.addNote(mStates.tempNote));
    return true;
  }

  @Override
  protected boolean onBackPressed() {
    return save();
  }

  public static class EditorStates extends StateHolder {
    public Note tempNote = new Note();
    public State<String> title = new State<>("");
    public State<String> content = new State<>("");
    public State<String> tip = new State<>(Utils.getApp().getString(R.string.edit));
    public State<String> time = new State<>(Utils.getApp().getString(R.string.new_note));
    public State<Boolean> titleRequestFocus = new State<>(false);
  }
}