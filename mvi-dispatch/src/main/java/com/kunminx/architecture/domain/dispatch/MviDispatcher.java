package com.kunminx.architecture.domain.dispatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.domain.event.Event;
import com.kunminx.architecture.domain.message.MutableResult;
import com.kunminx.architecture.domain.queue.FixedLengthList;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by KunMinX at 2022/7/3
 */
public class MviDispatcher<E extends Event> extends ViewModel implements DefaultLifecycleObserver {

  private final static int DEFAULT_QUEUE_LENGTH = 10;
  private final HashMap<String, LifecycleOwner> mOwner = new HashMap<>();
  private final HashMap<String, LifecycleOwner> mFragmentOwner = new HashMap<>();
  private final HashMap<String, Observer<E>> mObservers = new HashMap<>();
  private final FixedLengthList<MutableResult<E>> mResults = new FixedLengthList<>();

  protected int initQueueMaxLength() {
    return DEFAULT_QUEUE_LENGTH;
  }

  public final void output(@NonNull AppCompatActivity activity, @NonNull Observer<E> observer) {
    activity.getLifecycle().addObserver(this);
    outputTo(activity.getClass().getName(), activity, observer);
  }

  public final void output(@NonNull Fragment fragment, @NonNull Observer<E> observer) {
    fragment.getLifecycle().addObserver(this);
    this.mFragmentOwner.put(fragment.getClass().getName(), fragment);
    outputTo(fragment.getClass().getName(), fragment.getViewLifecycleOwner(), observer);
  }

  private void outputTo(String fullName, LifecycleOwner owner, Observer<E> observer) {
    this.mOwner.put(fullName, owner);
    this.mObservers.put(fullName, observer);
    for (MutableResult<E> result : mResults) {
      result.observe(owner, observer);
    }
  }

  protected final void sendResult(@NonNull E event) {
    mResults.init(initQueueMaxLength(), mutableResult -> {
      for (Map.Entry<String, Observer<E>> entry : mObservers.entrySet()) {
        Observer<E> observer = entry.getValue();
        mutableResult.removeObserver(observer);
      }
    });
    boolean eventExist = false;
    for (MutableResult<E> result : mResults) {
      int id1 = System.identityHashCode(result.getValue());
      int id2 = System.identityHashCode(event);
      if (id1 == id2) {
        eventExist = true;
        break;
      }
    }
    if (!eventExist) {
      MutableResult<E> result = new MutableResult<>(event);
      for (Map.Entry<String, Observer<E>> entry : mObservers.entrySet()) {
        String key = entry.getKey();
        Observer<E> observer = entry.getValue();
        LifecycleOwner owner = mOwner.get(key);
        assert owner != null;
        result.observe(owner, observer);
      }
      mResults.add(result);
    }

    MutableResult<E> result = null;
    for (MutableResult<E> r : mResults) {
      int id1 = System.identityHashCode(r.getValue());
      int id2 = System.identityHashCode(event);
      if (id1 == id2) {
        result = r;
        break;
      }
    }
    if (result != null) result.setValue(event);
  }

  public void input(E event) {

  }

  @Override
  public void onDestroy(@NonNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onDestroy(owner);

    boolean isFragment = owner instanceof Fragment;
    for (Map.Entry<String, LifecycleOwner> entry : isFragment ? mFragmentOwner.entrySet() : mOwner.entrySet()) {
      LifecycleOwner owner2 = entry.getValue();
      if (owner2.equals(owner)) {
        String key = entry.getKey();
        mOwner.remove(key);
        if (isFragment) mFragmentOwner.remove(key);
        for (MutableResult<E> mutableResult : mResults) {
          mutableResult.removeObserver(mObservers.get(key));
        }
        mObservers.remove(key);
        break;
      }
    }
    if (mObservers.size() == 0) {
      mResults.clear();
    }
  }
}

