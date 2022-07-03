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
  private Observer<E> mObserver;
  private final List<MutableResult<E>> results = new ArrayList<>();

  public void outPut(LifecycleOwner owner, Observer<E> observer) {
    this.mOwner = owner;
    this.mObserver = observer;
  }

  protected MutableResult<E> getResult(int eventId) {
    for (MutableResult<E> result : results) {
      if (Objects.requireNonNull(result.getValue()).eventId == eventId) {
        return result;
      }
    }
    return null;
  }

  public void input(E event) {
    boolean eventExist = false;
    for (MutableResult<E> result : results) {
      if (Objects.requireNonNull(result.getValue()).eventId == event.eventId) {
        eventExist = true;
        break;
      }
    }
    if (!eventExist) {
      MutableResult<E> result = new MutableResult<>(event);
      result.observe(mOwner, mObserver);
      results.add(result);
    }
  }
}
