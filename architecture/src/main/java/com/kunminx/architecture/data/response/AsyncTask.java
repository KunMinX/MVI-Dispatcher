package com.kunminx.architecture.data.response;

import android.annotation.SuppressLint;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by KunMinX at 2022/6/14
 */
public class AsyncTask {

  @SuppressLint("CheckResult")
  public static <T> Observable<T> doIO(Action<T> start) {
    return Observable.create((ObservableOnSubscribe<T>) start::onEmit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
  }

  @SuppressLint("CheckResult")
  public static <T> Observable<T> doCalculate(Action<T> start) {
    return Observable.create((ObservableOnSubscribe<T>) start::onEmit)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread());
  }

  public interface Action<T> {
    void onEmit(ObservableEmitter<T> emitter);
  }
}
