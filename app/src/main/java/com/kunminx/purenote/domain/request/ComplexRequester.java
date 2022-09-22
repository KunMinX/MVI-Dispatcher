package com.kunminx.purenote.domain.request;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.purenote.domain.intent.ComplexIntent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by KunMinX at 2022/7/5
 */
public class ComplexRequester extends MviDispatcher<ComplexIntent> {

  private Disposable mDisposable;

  /**
   * TODO tip 1：可初始化配置队列长度，自动丢弃队首过时消息
   */
  @Override
  protected int initQueueMaxLength() {
    return 5;
  }

  /**
   * TODO tip 2：
   *  此为领域层组件，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  可为同业务不同页面复用。
   *  ~
   *  与此同时，本组件高度封装，
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
  protected void onHandle(ComplexIntent intent) {
    switch (intent.id) {
      case ComplexIntent.Test1.ID:

        //TODO tip 3: 定长队列，随取随用，绝不丢失事件
        // 此处通过 RxJava 轮询模拟事件连发，可于 Logcat Debug 见输出
        // ~
        // Fixed length queue, on demand, never lose events
        // Here, rxjava polling simulation events are sent repeatedly, and the output can be seen in logcat debug

        if (mDisposable == null)
          mDisposable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(aLong -> input(ComplexIntent.Test4(aLong.intValue())));
        break;
      case ComplexIntent.Test2.ID:
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> sendResult(intent));
        break;
      case ComplexIntent.Test3.ID:
        sendResult(intent);
        break;
      case ComplexIntent.Test4.ID:
        ComplexIntent.Test4 test4 = (ComplexIntent.Test4) intent;
        sendResult(test4.copy(test4.paramCount));
        break;
    }
  }
}