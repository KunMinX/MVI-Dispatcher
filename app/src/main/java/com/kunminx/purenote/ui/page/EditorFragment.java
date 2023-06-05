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
import com.kunminx.purenote.domain.intent.Messages;
import com.kunminx.purenote.domain.intent.NoteIntent;
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

  /**
   * TODO tip 1：
   * 通过 PublishSubject 接收数据，并在唯一出口 output{ ... } 中响应数据的变化，
   * 通过 BehaviorSubject 通知所绑定控件属性重新渲染，并为其兜住最后一次状态，
   */
  @Override
  protected void onOutput() {
    mNoteRequester.output(this, noteIntent -> {
      if (Objects.equals(noteIntent.id, NoteIntent.InitItem.ID)) {
        mStates.tempNote.set(((NoteIntent.InitItem) noteIntent).paramNote);
        Note tempNote = Objects.requireNonNull(mStates.tempNote.get());
        mStates.title.set(tempNote.getTitle());
        mStates.content.set(tempNote.getContent());
        if (TextUtils.isEmpty(tempNote.getId())) {
          mStates.titleRequestFocus.set(true);
        } else {
          mStates.tip.set(getString(R.string.last_time_modify));
          mStates.time.set(tempNote.getModifyDate());
        }
      } else if (Objects.equals(noteIntent.id, NoteIntent.AddItem.ID)) {
        mMessenger.input(Messages.RefreshNoteList());
        ToastUtils.showShortToast(getString(R.string.saved));
        nav().navigateUp();
      }
    });
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 input() 发消息至 "可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  @Override
  protected void onInput() {
    mClickProxy.setOnClickListener(v -> {
      if (v.getId() == R.id.btn_back) save();
    });
    if (getArguments() != null)
      mNoteRequester.input(NoteIntent.InitItem(getArguments().getParcelable(NOTE)));
  }

  private void save() {
    Note tempNote = Objects.requireNonNull(mStates.tempNote.get());
    String title = mStates.title.get();
    String content = mStates.content.get();
    if (TextUtils.isEmpty(title + content) || tempNote.getTitle().equals(title) && tempNote.getContent().equals(content)) {
      nav().navigateUp();
      return;
    }
    Note note;
    long time = System.currentTimeMillis();
    if (TextUtils.isEmpty(tempNote.getId())) {
      note = new Note(UUID.randomUUID().toString(), title, content, time, time, 0);
    } else {
      note = new Note(tempNote.getId(), title, content, tempNote.getCreateTime(), time, tempNote.getType());
    }
    mNoteRequester.input(NoteIntent.AddItem(note));
  }

  @Override
  protected void onBackPressed() {
    save();
  }

  /**
   * TODO tip 3：
   * 基于单一职责原则，抽取 Jetpack ViewModel "状态保存和恢复" 的能力作为 StateHolder，
   * 并使用 ObservableField 的改良版子类 State 来承担 BehaviorSubject，用作所绑定控件的 "可信数据源"，
   * 从而在收到来自 PublishSubject 的结果回推后，响应结果数据的变化，也即通知控件属性重新渲染，并为其兜住最后一次状态，
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