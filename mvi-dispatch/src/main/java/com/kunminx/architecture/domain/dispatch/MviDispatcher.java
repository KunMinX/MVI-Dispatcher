package com.kunminx.architecture.domain.dispatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.domain.event.Event;
import com.kunminx.architecture.domain.message.MutableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Create by KunMinX at 2022/7/3
 */
public class MviDispatcher<E extends Event> extends ViewModel {

  private final HashMap<String, LifecycleOwner> mOwner = new HashMap<>();
  private final HashMap<String, Observer<E>> mObservers = new HashMap<>();
  private final List<MutableResult<E>> mResults = new ArrayList<>();

  public final void output(@NonNull AppCompatActivity activity, @NonNull Observer<E> observer) {
    outputTo(activity.getClass().getName(), activity, observer);
  }

  public final void output(@NonNull Fragment fragment, @NonNull Observer<E> observer) {
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
    boolean eventExist = false;
    for (MutableResult<E> result : mResults) {
      if (Objects.requireNonNull(result.getValue()).eventId == event.eventId) {
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
      if (Objects.requireNonNull(r.getValue()).eventId == event.eventId) {
        result = r;
        break;
      }
    }
    if (result != null) result.setValue(event);
  }

  public void input(E event) {

  }

  @Override
  protected void onCleared() {
    super.onCleared();
    mObservers.clear();
    mResults.clear();
    mOwner.clear();
  }
}
