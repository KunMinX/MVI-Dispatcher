package com.kunminx.purenote.domain.request;

import android.text.TextUtils;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.purenote.data.repo.DataRepository;
import com.kunminx.purenote.domain.event.ApiEvent;
/**
 * Create by KunMinX at 2022/8/24
 */
public class HttpRequester extends MviDispatcher<ApiEvent> {
  @Override
  protected void onHandle(ApiEvent event) {
    switch (event.api) {
      case ApiEvent.GET_WEATHER_INFO:
        DataRepository.getInstance().getWeather(event.api, event.param.cityCode, dataResult -> {
          event.result.live = dataResult.getResult();
          String errorMsg = dataResult.getResponseStatus().getMsg();
          if (TextUtils.isEmpty(errorMsg)) sendResult(event);
          else input(ApiEvent.onError(errorMsg));
        });
        break;
      case ApiEvent.ERROR:
        sendResult(event);
        break;
    }
  }
}