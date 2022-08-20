package com.kunminx.architecture.domain.result;

import static androidx.lifecycle.Lifecycle.State.DESTROYED;
import static androidx.lifecycle.Lifecycle.State.STARTED;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.Iterator;
import java.util.Map;

/**
 * Create by KunMinX at 2022/8/16
 */

public abstract class Result<T> {

  private static final int START_VERSION = -1;
  private static final Object NOT_SET = new Object();

  private final SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers = new SafeIterableMap<>();
  private int mActiveCount = 0;
  private boolean mChangingActiveState;
  private volatile Object mData;
  private int mVersion;
  private int mCurrentVersion = START_VERSION;

  private boolean mDispatchingValue;
  private boolean mDispatchInvalidated;

  public Result(T value) {
    mData = value;
    mVersion = START_VERSION + 1;
  }

  public Result() {
    mData = NOT_SET;
    mVersion = START_VERSION;
  }

  @SuppressWarnings("unchecked")
  private void considerNotify(ObserverWrapper observer) {
    if (!observer.mActive) return;
    if (!observer.shouldBeActive()) {
      observer.activeStateChanged(false);
      return;
    }
    if (observer.mLastVersion >= mVersion) return;
    observer.mLastVersion = mVersion;
    observer.mObserver.onChanged((T) mData);
  }

  void dispatchingValue(@Nullable ObserverWrapper initiator) {
    if (mDispatchingValue) {
      mDispatchInvalidated = true;
      return;
    }
    mDispatchingValue = true;
    do {
      mDispatchInvalidated = false;
      if (initiator != null) {
        considerNotify(initiator);
        initiator = null;
      } else {
        for (Iterator<Map.Entry<Observer<? super T>, ObserverWrapper>> iterator =
             mObservers.iteratorWithAdditions(); iterator.hasNext(); ) {
          considerNotify(iterator.next().getValue());
          if (mDispatchInvalidated) break;
        }
      }
    } while (mDispatchInvalidated);
    mDispatchingValue = false;
  }

  @MainThread
  public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
    if (owner.getLifecycle().getCurrentState() == DESTROYED) return;
    LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
    ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
    if (existing != null && !existing.isAttachedTo(owner))
      throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
    if (existing != null) return;
    mCurrentVersion = mVersion;
    owner.getLifecycle().addObserver(wrapper);
  }

  @MainThread
  public void removeObserver(@NonNull final Observer<? super T> observer) {
    ObserverWrapper removed = mObservers.remove(observer);
    if (removed == null) return;
    removed.detachObserver();
    removed.activeStateChanged(false);
  }

  @MainThread
  public void removeObservers(@NonNull final LifecycleOwner owner) {
    for (Map.Entry<Observer<? super T>, ObserverWrapper> entry : mObservers) {
      if (entry.getValue().isAttachedTo(owner)) removeObserver(entry.getKey());
    }
  }

  @MainThread
  protected void setValue(T value) {
    mVersion++;
    mData = value;
    dispatchingValue(null);
  }

  @SuppressWarnings("unchecked")
  @Nullable
  public T getValue() {
    Object data = mData;
    if (data != NOT_SET) return (T) data;
    return null;
  }

  int getVersion() {
    return mVersion;
  }

  protected void onActive() {

  }

  protected void onInactive() {

  }

  public boolean hasObservers() {
    return mObservers.size() > 0;
  }

  public boolean hasActiveObservers() {
    return mActiveCount > 0;
  }

  @MainThread
  void changeActiveCounter(int change) {
    int previousActiveCount = mActiveCount;
    mActiveCount += change;
    if (mChangingActiveState) return;
    mChangingActiveState = true;
    try {
      while (previousActiveCount != mActiveCount) {
        boolean needToCallActive = previousActiveCount == 0 && mActiveCount > 0;
        boolean needToCallInactive = previousActiveCount > 0 && mActiveCount == 0;
        previousActiveCount = mActiveCount;
        if (needToCallActive) onActive();
        else if (needToCallInactive) onInactive();
      }
    } finally {
      mChangingActiveState = false;
    }
  }

  class LifecycleBoundObserver extends ObserverWrapper implements LifecycleEventObserver {
    @NonNull
    final LifecycleOwner mOwner;

    LifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<? super T> observer) {
      super(observer);
      mOwner = owner;
    }

    @Override
    boolean shouldBeActive() {
      return mOwner.getLifecycle().getCurrentState().isAtLeast(STARTED);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
      Lifecycle.State currentState = mOwner.getLifecycle().getCurrentState();
      if (currentState == DESTROYED) {
        removeObserver(mObserver);
        return;
      }
      Lifecycle.State prevState = null;
      while (prevState != currentState) {
        prevState = currentState;
        activeStateChanged(shouldBeActive());
        currentState = mOwner.getLifecycle().getCurrentState();
      }
    }

    @Override
    boolean isAttachedTo(LifecycleOwner owner) {
      return mOwner == owner;
    }

    @Override
    void detachObserver() {
      mOwner.getLifecycle().removeObserver(this);
    }
  }

  private abstract class ObserverWrapper {
    final Observer<? super T> mObserver;
    boolean mActive;
    int mLastVersion = START_VERSION;

    ObserverWrapper(Observer<? super T> observer) {
      mObserver = observer;
    }

    abstract boolean shouldBeActive();

    boolean isAttachedTo(LifecycleOwner owner) {
      return false;
    }

    void detachObserver() {
    }

    void activeStateChanged(boolean newActive) {
      if (newActive == mActive) return;
      mActive = newActive;
      changeActiveCounter(mActive ? 1 : -1);
      if (mActive && mVersion > mCurrentVersion) dispatchingValue(this);
    }
  }
}

