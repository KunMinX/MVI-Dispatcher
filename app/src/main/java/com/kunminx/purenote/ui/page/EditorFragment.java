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
import com.kunminx.purenote.domain.event.NoteIntent;
import com.kunminx.purenote.domain.message.PageMessenger;
import com.kunminx.purenote.domain.request.NoteRequester;

import java.util.Objects;
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
      mStates.tempNote.set(getArguments().getParcelable(NOTE));
      Note tempNote = Objects.requireNonNull(mStates.tempNote.get());
      mStates.title.set(tempNote.getTitle());
      mStates.content.set(tempNote.getContent());
      if (TextUtils.isEmpty(tempNote.getId())) {
        mStates.titleRequestFocus.set(true);
      } else {
        mStates.tip.set(getString(R.string.last_time_modify));
        mStates.time.set(tempNote.getModifyDate());
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
    mNoteRequester.output(this, noteIntent -> {
      if (Objects.equals(noteIntent.id, NoteIntent.AddItem.ID)) {
        mMessenger.input(Messages.RefreshNoteList());
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
    Note tempNote = Objects.requireNonNull(mStates.tempNote.get());
    String title = mStates.title.get();
    String content = mStates.content.get();
    if (TextUtils.isEmpty(title + content) || tempNote.getTitle().equals(title) && tempNote.getContent().equals(content)) {
      return nav().navigateUp();
    }
    Note note;
    long time = System.currentTimeMillis();
    if (TextUtils.isEmpty(tempNote.getId())) {
      note = new Note(UUID.randomUUID().toString(), title, content, time, time, 0);
    } else {
      note = new Note(tempNote.getId(), title, content, tempNote.getCreateTime(), time, tempNote.getType());
    }
    mNoteRequester.input(NoteIntent.AddItem(note));
    return true;
  }

  @Override
  protected boolean onBackPressed() {
    return save();
  }

  /**
   * TODO tip 3：本项目为三层架构，即 表现层、领域层、数据层
   *  StateHolder 属于表现层，MVI-Dispatcher 属于领域层，
   *  领域层组件通过 PublishSubject（例如 SharedFlow）分发结果至表现层，
   *  对于状态，由 BehaviorSubject（例如以下 State 组件）响应和兜着；对于事件，则一次性执行，
   *
   * 具体可参见《解决 MVI 实战痛点》解析
   * https://juejin.cn/post/7134594010642907149
   */
  public static class EditorStates extends StateHolder {
    public final State<Note> tempNote = new State<>(new Note());
    public final State<String> title = new State<>("");
    public final State<String> content = new State<>("");
    public final State<String> tip = new State<>(Utils.getApp().getString(R.string.edit));
    public final State<String> time = new State<>(Utils.getApp().getString(R.string.new_note));
    public final State<Boolean> titleRequestFocus = new State<>(false);
  }
}