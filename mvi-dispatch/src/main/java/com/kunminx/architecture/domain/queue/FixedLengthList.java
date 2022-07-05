package com.kunminx.architecture.domain.queue;

import java.util.LinkedList;

/**
 * Create by KunMinX at 2022/7/5
 */
public class FixedLengthList<T> extends LinkedList<T> {
  private int maxLength;
  private boolean hasBeenInit;

  public final void setMaxLength(int maxLength) {
    if (!hasBeenInit) {
      this.maxLength = maxLength;
      hasBeenInit = true;
    }
  }

  @Override
  public boolean add(T t) {
    if (size() + 1 > maxLength) super.removeFirst();
    return super.add(t);
  }
}
