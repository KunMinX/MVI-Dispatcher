package com.kunminx.purenote;

import android.app.Application;

import com.kunminx.architecture.utils.Utils;

/**
 * Create by KunMinX at 2022/7/3
 */
public class App extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Utils.init(this);
  }
}
