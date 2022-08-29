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
        DataRepository.getInstance().getWeatherInfo(event.api, event.param.cityCode, dataResult -> {
          String errorMsg = dataResult.getResponseStatus().getMsg();
          if (TextUtils.isEmpty(errorMsg))
            sendResult(ApiEvent.copy(event, new ApiEvent.Result(dataResult.getResult())));
          else input(ApiEvent.onError(event, errorMsg));
        });
        break;
      case ApiEvent.ERROR:
        sendResult(event);
        break;
    }
  }
}