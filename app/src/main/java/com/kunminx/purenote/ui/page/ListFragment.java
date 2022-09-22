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
import com.kunminx.purenote.domain.intent.Api;
import com.kunminx.purenote.domain.intent.Messages;
import com.kunminx.purenote.domain.intent.NoteIntent;
import com.kunminx.purenote.domain.message.PageMessenger;
import com.kunminx.purenote.domain.request.HttpRequester;
import com.kunminx.purenote.domain.request.NoteRequester;
import com.kunminx.purenote.ui.adapter.NoteAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
      if (Objects.equals(messages.id, Messages.RefreshNoteList.ID)) {
        mNoteRequester.input(NoteIntent.GetNoteList());
      }
    });

    mNoteRequester.output(this, noteIntent -> {
      switch (noteIntent.id) {
        case NoteIntent.ToppingItem.ID:
        case NoteIntent.GetNoteList.ID:
          NoteIntent.GetNoteList getNoteList = (NoteIntent.GetNoteList) noteIntent;
          mAdapter.refresh(getNoteList.resultNotes);
          mStates.emptyViewShow.set(mStates.list.size() == 0);
          break;
        case NoteIntent.MarkItem.ID:
        case NoteIntent.RemoveItem.ID:
          break;
      }
    });

    mHttpRequester.output(this, api -> {
      switch (api.id) {
        case Api.GetWeatherInfo.ID:
          Api.GetWeatherInfo weatherInfo = (Api.GetWeatherInfo) api;
          Weather.Live live = weatherInfo.resultLive;
          if (live != null) mStates.weather.set(live.getWeather());
          break;
        case Api.OnError.ID:
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
      if (viewId == R.id.btn_mark) mNoteRequester.input(NoteIntent.MarkItem(item));
      else if (viewId == R.id.btn_topping) mNoteRequester.input(NoteIntent.ToppingItem(item));
      else if (viewId == R.id.btn_delete) mNoteRequester.input(NoteIntent.RemoveItem(item));
      else if (viewId == R.id.cl) EditorFragment.start(nav(), item);
    });
    mClickProxy.setOnClickListener(view -> {
      if (view.getId() == R.id.fab) EditorFragment.start(nav(), new Note());
      else if (view.getId() == R.id.iv_logo) nav().navigate(R.id.action_list_to_setting);
    });
    if (TextUtils.isEmpty(mStates.weather.get())) {
      mStates.loadingWeather.set(true);
      mHttpRequester.input(Api.GetWeatherInfo(HttpRequester.CITY_CODE_BEIJING));
    }
    if (mStates.list.isEmpty()) mNoteRequester.input(NoteIntent.GetNoteList());
  }

  @Override
  protected boolean onBackPressed() {
    mMessenger.input(Messages.FinishActivity());
    return super.onBackPressed();
  }

  /**
   * TODO tip 3：传统 MVI 属于对响应式编程的填坑和升级，
   *  故通常是两层架构：表现层和数据层，
   *  ViewModel 是表现层组件，业务逻辑状态改变都在 ViewModel 中写，
   *  响应式编程便于单元测试和关注点分离，同时也使同质化的业务逻辑分散在多个 ViewModel 中，造成修改时的不一致，
   *  ~
   *  故综合考虑，本项目示例采用三层架构，即 表现层、领域层、数据层，
   *  StateHolder 属于表现层，MVI-Dispatcher 属于领域层，
   *  领域层组件通过 PublishSubject（例如 SharedFlow）分发结果至表现层，
   *  对于状态，由 BehaviorSubject（例如以下 State 组件）响应和兜着；对于事件，则一次性执行，
   *
   * 具体可参见《解决 MVI 实战痛点》解析
   * https://juejin.cn/post/7134594010642907149
   */
  public static class ListStates extends StateHolder {
    public final List<Note> list = new ArrayList<>();
    public final State<Boolean> emptyViewShow = new State<>(false);
    public final State<Boolean> loadingWeather = new State<>(false);
    public final State<String> weather = new State<>("");
  }
}