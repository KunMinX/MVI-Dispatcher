package com.kunminx.purenote.domain.request;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.purenote.domain.event.ComplexEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by KunMinX at 2022/7/5
 */
public class ComplexRequester extends MviDispatcher<ComplexEvent> {

  /**
   * TODO tip 1：可初始化配置队列长度，自动丢弃队首过时消息
   */
  @Override
  protected int initQueueMaxLength() {
    return 20;
  }

  /**
   * TODO tip 2：
   *  作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  ~
   *  与此同时，作为唯一可信源成熟态，
   *  一举消除 “mutable 样板代码 + LiveData 连发事件覆盖 + LiveData.setValue 误用滥用” 高频痛点。
   */
  @Override
  public void input(ComplexEvent event) {
    super.input(event);

    switch (event.eventId) {
      case ComplexEvent.EVENT_TEST_1:
      case ComplexEvent.EVENT_TEST_3:
        sendResult(event);
        break;
      case ComplexEvent.EVENT_TEST_2:
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                  sendResult(event);
                });
        break;
    }
  }
}