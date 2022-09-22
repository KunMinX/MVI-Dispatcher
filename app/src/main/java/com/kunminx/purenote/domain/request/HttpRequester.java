package com.kunminx.purenote.domain.request;

import android.text.TextUtils;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.purenote.data.repo.DataRepository;
import com.kunminx.purenote.domain.intent.Api;
/**
 * Create by KunMinX at 2022/8/24
 */
public class HttpRequester extends MviDispatcher<Api> {
  public final static String CITY_CODE_BEIJING = "110000";

  /**
   * TODO tip 1：
   *  此为领域层组件，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  可为同业务不同页面复用。
   *  ~
   *  与此同时，作为唯一可信源成熟态，
   *  自动消除 “mutable 样板代码 + LiveData 连发事件覆盖 + LiveData.setValue 误用滥用” 高频痛点。
   *  ~
   *  ~
   *  As the 'only credible source', it receives messages sent from the page,
   *  processes the business logic internally, and distributes them through sendResult results.
   *  ~
   *  At the same time, as the adult stage of Single Source of Truth,
   *  automatically eliminates the high-frequency pain spots of "mutable boilerplate code
   *  & Livedata serial event coverage & mutableLiveData.setValue abuse".
   */
  @Override
  protected void onHandle(Api intent) {
    switch (intent.id) {
      case Api.OnLoading.ID:
      case Api.OnError.ID:
        sendResult(intent);
        break;
      case Api.GetWeatherInfo.ID:
        input(Api.OnLoading(true));
        Api.GetWeatherInfo getWeatherInfo = (Api.GetWeatherInfo) intent;
        DataRepository.getInstance().getWeatherInfo(getWeatherInfo.paramCityCode, dataResult -> {
          String errorMsg = dataResult.getResponseStatus().getMsg();
          if (TextUtils.isEmpty(errorMsg)) sendResult(getWeatherInfo.copy(dataResult.getResult()));
          else input(Api.OnError(errorMsg));
          input(Api.OnLoading(false));
        });
        break;
    }
  }
}