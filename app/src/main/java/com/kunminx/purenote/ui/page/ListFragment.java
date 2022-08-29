package com.kunminx.purenote.ui.page;

import android.text.TextUtils;

import com.kunminx.architecture.domain.dispatch.GlobalConfigs;
import com.kunminx.architecture.ui.bind.ClickProxy;
import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.architecture.ui.state.State;
import com.kunminx.purenote.BR;
import com.kunminx.purenote.R;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.data.bean.Weather;
import com.kunminx.purenote.data.config.Key;
import com.kunminx.purenote.domain.event.ApiEvent;
import com.kunminx.purenote.domain.event.Messages;
import com.kunminx.purenote.domain.event.NoteEvent;
import com.kunminx.purenote.domain.message.PageMessenger;
import com.kunminx.purenote.domain.request.HttpRequester;
import com.kunminx.purenote.domain.request.NoteRequester;
import com.kunminx.purenote.ui.adapter.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by KunMinX at 2022/6/30
 */
public class ListFragment extends BaseFragment {
  private ListStates mStates;
  private NoteRequester mNoteRequester;
  private HttpRequester mHttpRequester;
  private PageMessenger mMessenger;
  private NoteAdapter mAdapter;
  private ClickProxy mClickProxy;

  @Override
  protected void initViewModel() {
    mStates = getFragmentScopeViewModel(ListStates.class);
    mNoteRequester = getFragmentScopeViewModel(NoteRequester.class);
    mHttpRequester = getFragmentScopeViewModel(HttpRequester.class);
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);
  }

  @Override
  protected DataBindingConfig getDataBindingConfig() {
    return new DataBindingConfig(R.layout.fragment_list, BR.state, mStates)
            .addBindingParam(BR.adapter, mAdapter = new NoteAdapter(mStates.list))
            .addBindingParam(BR.click, mClickProxy = new ClickProxy());
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
        mNoteRequester.input(NoteEvent.getList());
      }
    });

    mNoteRequester.output(this, noteEvent -> {
      switch (noteEvent.eventId) {
        case NoteEvent.EVENT_TOPPING_ITEM:
        case NoteEvent.EVENT_GET_NOTE_LIST:
          mAdapter.refresh(noteEvent.result.notes);
          mStates.emptyViewShow.set(mStates.list.size() == 0);
          break;
        case NoteEvent.EVENT_MARK_ITEM:
        case NoteEvent.EVENT_REMOVE_ITEM:
          break;
      }
    });

    mHttpRequester.output(this, apiEvent -> {
      switch (apiEvent.api) {
        case ApiEvent.GET_WEATHER_INFO:
          Weather.Live live = apiEvent.result.live;
          if (live != null) mStates.weather.set(live.getWeather());
          break;
        case ApiEvent.ERROR:
          break;
      }
      mStates.loadingWeather.set(false);
    });

    //TODO tip 3: 更新配置并刷新界面，是日常开发高频操作，
    // 当别处通过 GlobalConfigs 为某配置 put 新值，此处响应并刷新 UI

    GlobalConfigs.output(this, keyValueEvent -> {
      switch (keyValueEvent.currentKey) {
        case Key.TEST_STRING:
          break;
        case Key.TEST_BOOLEAN:
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
    mAdapter.setOnItemClickListener((viewId, item, position) -> {
      if (viewId == R.id.btn_mark) mNoteRequester.input(NoteEvent.markNote(item));
      else if (viewId == R.id.btn_topping) mNoteRequester.input(NoteEvent.toppingNote(item));
      else if (viewId == R.id.btn_delete) mNoteRequester.input(NoteEvent.removeNote(item));
      else if (viewId == R.id.cl) EditorFragment.start(nav(), item);
    });
    mClickProxy.setOnClickListener(view -> {
      if (view.getId() == R.id.fab) EditorFragment.start(nav(), new Note());
      else if (view.getId() == R.id.iv_logo) nav().navigate(R.id.action_list_to_setting);
    });
    if (TextUtils.isEmpty(mStates.weather.get())) {
      mStates.loadingWeather.set(true);
      mHttpRequester.input(ApiEvent.getWeather(ApiEvent.CITY_CODE_BEIJING));
    }
    if (mStates.list.isEmpty()) mNoteRequester.input(NoteEvent.getList());
  }

  @Override
  protected boolean onBackPressed() {
    mMessenger.input(new Messages(Messages.EVENT_FINISH_ACTIVITY));
    return super.onBackPressed();
  }

  public static class ListStates extends StateHolder {
    public final List<Note> list = new ArrayList<>();
    public final State<Boolean> emptyViewShow = new State<>(false);
    public final State<Boolean> loadingWeather = new State<>(false);
    public final State<String> weather = new State<>("");
  }
}