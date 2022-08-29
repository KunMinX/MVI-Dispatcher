package com.kunminx.purenote.domain.request;

import android.text.TextUtils;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.purenote.data.repo.DataRepository;
import com.kunminx.purenote.domain.event.Api;
/**
 * Create by KunMinX at 2022/8/24
 */
public class HttpRequester extends MviDispatcher<Api> {
  @Override
  protected void onHandle(Api event) {
    switch (event.node) {
      case Api.GET_WEATHER_INFO:
        DataRepository.getInstance().getWeatherInfo(event.node, event.param.cityCode, dataResult -> {
          String errorMsg = dataResult.getResponseStatus().getMsg();
          if (TextUtils.isEmpty(errorMsg))
            sendResult(event.copy(new Api.Result(dataResult.getResult())));
          else input(Api.onError(event, errorMsg));
        });
        break;
      case Api.ERROR:
        sendResult(event);
        break;
    }
  }
}