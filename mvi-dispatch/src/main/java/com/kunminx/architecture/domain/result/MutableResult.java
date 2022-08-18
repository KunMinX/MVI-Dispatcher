package com.kunminx.architecture.domain.result;

/**
 * Create by KunMinX at 2022/8/17
 */
public class MutableResult<T> extends Result<T> {

  public MutableResult(T value) {
    super(value);
  }

  public MutableResult() {
    super();
  }

  @Override
  public void setValue(T value) {
    super.setValue(value);
  }
}
