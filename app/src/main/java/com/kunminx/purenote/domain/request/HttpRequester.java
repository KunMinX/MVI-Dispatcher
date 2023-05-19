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
   *  本组件通过封装，默使数据从 "领域层" 到 "表现层" 单向流动，
   *  消除 “mutable 样板代码 + 连发事件覆盖 + mutable.setValue 误用滥用” 等高频痛点。
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