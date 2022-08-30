package com.kunminx.purenote.domain.request;

import android.text.TextUtils;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.purenote.data.repo.DataRepository;
import com.kunminx.purenote.domain.event.Api;
/**
 * Create by KunMinX at 2022/8/24
 */
public class HttpRequester extends MviDispatcher<Api> {
  public final static String CITY_CODE_BEIJING = "110000";

  @Override
  protected void onHandle(Api intent) {
    switch (intent.id) {
      case Api.GetWeatherInfo.ID:
        Api.GetWeatherInfo getWeatherInfo = (Api.GetWeatherInfo) intent;
        DataRepository.getInstance().getWeatherInfo(getWeatherInfo.paramCityCode, dataResult -> {
          String errorMsg = dataResult.getResponseStatus().getMsg();
          if (TextUtils.isEmpty(errorMsg)) sendResult(getWeatherInfo.copy(dataResult.getResult()));
          else input(Api.OnError(errorMsg));
        });
        break;
      case Api.OnError.ID:
        sendResult(intent);
        break;
    }
  }
}