package com.kunminx.architecture.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.core.content.FileProvider;

import java.lang.reflect.InvocationTargetException;

/**
 * <pre>
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : utils about initialization
 * </pre>
 */
public final class Utils {

  @SuppressLint("StaticFieldLeak")
  private static Application sApplication;

  private Utils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  public static void init(final Context context) {
    if (context == null) {
      init(getApplicationByReflect());
      return;
    }
    init((Application) context.getApplicationContext());
  }

  public static void init(final Application app) {
    if (sApplication == null) {
      if (app == null) {
        sApplication = getApplicationByReflect();
      } else {
        sApplication = app;
      }
    } else {
      if (app != null && app.getClass() != sApplication.getClass()) {
        sApplication = app;
      }
    }
  }

  public static Application getApp() {
    if (sApplication != null) {
      return sApplication;
    }
    Application app = getApplicationByReflect();
    init(app);
    return app;
  }

  private static Application getApplicationByReflect() {
    try {
      @SuppressLint("PrivateApi")
      Class<?> activityThread = Class.forName("android.app.ActivityThread");
      Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
      Object app = activityThread.getMethod("getApplication").invoke(thread);
      if (app == null) {
        throw new NullPointerException("u should init first");
      }
      return (Application) app;
    } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
      e.printStackTrace();
    }
    throw new NullPointerException("u should init first");
  }

  public static final class FileProvider4UtilCode extends FileProvider {
    @Override
    public boolean onCreate() {
      Utils.init(getContext());
      return true;
    }
  }
}
