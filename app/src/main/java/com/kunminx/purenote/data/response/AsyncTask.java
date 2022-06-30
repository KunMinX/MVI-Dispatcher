package com.kunminx.purenote.data.response;

import android.annotation.SuppressLint;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by KunMinX at 2022/6/14
 */
public class AsyncTask {

  @SuppressLint("CheckResult")
  public static <T> void doAction(ActionStart<T> start, ActionEnd<T> end) {
    Observable.create((ObservableOnSubscribe<T>) emitter -> emitter.onNext(start.getData()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(end::onResult);
  }

  public interface ActionStart<T> {
    T getData();
  }

  public interface ActionEnd<T> {
    void onResult(T t);
  }
}
