package com.kunminx.architecture.domain.dispatch;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.domain.event.Event;
import com.kunminx.architecture.domain.message.MutableResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Create by KunMinX at 2022/7/3
 */
public class TruthDispatcher<E extends Event> extends ViewModel {

  private LifecycleOwner mOwner;
  private final List<Observer<E>> mObservers = new ArrayList<>();
  private final List<MutableResult<E>> mResults = new ArrayList<>();

  public void output(LifecycleOwner owner, Observer<E> observer) {
    this.mOwner = owner;
    this.mObservers.add(observer);
  }

  protected void sendResult(E event) {
    MutableResult<E> result = null;
    for (MutableResult<E> r : mResults) {
      if (Objects.requireNonNull(r.getValue()).eventId == event.eventId) {
        result = r;
        break;
      }
    }
    if (result != null) result.setValue(event);
  }

  public void input(E event) {
    boolean eventExist = false;
    for (MutableResult<E> result : mResults) {
      if (Objects.requireNonNull(result.getValue()).eventId == event.eventId) {
        eventExist = true;
        break;
      }
    }
    if (!eventExist) {
      MutableResult<E> result = new MutableResult<>(event);
      for (Observer<E> observer : mObservers) {
        result.observe(mOwner, observer);
      }
      mResults.add(result);
    }
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    mObservers.clear();
    mResults.clear();
    mOwner = null;
  }
}
