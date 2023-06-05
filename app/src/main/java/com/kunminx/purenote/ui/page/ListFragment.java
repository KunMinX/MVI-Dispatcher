package com.kunminx.purenote.ui.page;

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
import com.kunminx.purenote.domain.request.WeatherRequester;
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
  private WeatherRequester mWeatherRequester;
  private PageMessenger mMessenger;
  private NoteAdapter mAdapter;
  private ClickProxy mClickProxy;

  @Override
  protected void initViewModel() {
    mStates = getFragmentScopeViewModel(ListStates.class);
    mNoteRequester = getFragmentScopeViewModel(NoteRequester.class);
    mWeatherRequester = getFragmentScopeViewModel(WeatherRequester.class);
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
   * 通过 PublishSubject 接收数据，并在唯一出口 output{ ... } 中响应数据的变化，
   * 通过 BehaviorSubject 通知所绑定控件属性重新渲染，并为其兜住最后一次状态，
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

    mWeatherRequester.output(this, api -> {
      switch (api.id) {
        case Api.OnLoading.ID:
          mStates.loadingWeather.set(((Api.OnLoading) api).resultIsLoading);
          break;
        case Api.GetWeatherInfo.ID:
          Api.GetWeatherInfo weatherInfo = (Api.GetWeatherInfo) api;
          Weather.Live live = weatherInfo.resultLive;
          if (live != null) mStates.weather.set(live.getWeather());
          break;
        case Api.OnError.ID:
          break;
      }
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
   * 通过唯一入口 input() 发消息至 "可信源"，由其内部统一处理业务逻辑和结果分发。
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

    //TODO 天气示例使用高德 API_KEY，如有需要，请自行在 "高德开放平台" 获取和在 DataRepository 类填入
    //    if (TextUtils.isEmpty(mStates.weather.get())) {
    //      mHttpRequester.input(Api.GetWeatherInfo(HttpRequester.CITY_CODE_BEIJING));
    //    }

    if (mStates.list.isEmpty()) mNoteRequester.input(NoteIntent.GetNoteList());
  }

  @Override
  protected void onBackPressed() {
    mMessenger.input(Messages.FinishActivity());
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
  public static class ListStates extends StateHolder {
    public final List<Note> list = new ArrayList<>();
    public final State<Boolean> emptyViewShow = new State<>(false);
    public final State<Boolean> loadingWeather = new State<>(false);
    public final State<String> weather = new State<>("");
  }
}