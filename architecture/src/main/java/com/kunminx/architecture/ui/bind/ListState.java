package com.kunminx.architecture.ui.bind;

import com.kunminx.architecture.ui.state.State;

import java.util.ArrayList;
import java.util.List;
/**
 * Create by KunMinX at 2022/8/18
 */
public class ListState<T> extends State<List<T>> {

  public ListState() {
    super(new ArrayList<T>());
  }

  public void refresh(List<T> list) {
    get().clear();
    get().addAll(list);
    set(get());
  }

  public List<T> getList() {
    return get();
  }

  public void appendList(List<T> list) {
    get().addAll(list);
    set(get());
  }

  public int size() {
    return get().size();
  }

  public boolean isEmpty() {
    return get().isEmpty();
  }
}
