package com.kunminx.architecture.ui.bind;

import android.view.View;
/**
 * Create by KunMinX at 2022/8/18
 */
public class ClickProxy implements View.OnClickListener {
  public View.OnClickListener listener;

  public void setOnClick(View.OnClickListener listener) {
    this.listener = listener;
  }
  @Override
  public void onClick(View view) {
    listener.onClick(view);
  }
}
