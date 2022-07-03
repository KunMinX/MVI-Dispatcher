package com.kunminx.purenote;

import com.kunminx.architecture.BaseApplication;
import com.kunminx.architecture.utils.Utils;

/**
 * Create by KunMinX at 2022/7/3
 */
public class App extends BaseApplication {

  @Override
  public void onCreate() {
    super.onCreate();
    Utils.init(this);
  }
}
