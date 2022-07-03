package com.kunminx.architecture.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/23
 *     desc  : AdaptScreenUtils
 * </pre>
 */
public final class AdaptScreenUtils {

  private static boolean isInitMiui = false;
  private static Field mTmpMetricsField;

  public static Resources adaptWidth(Resources resources, int designWidth) {
    DisplayMetrics dm = getDisplayMetrics(resources);
    float newXdpi = dm.xdpi = (dm.widthPixels * 72f) / designWidth;
    setAppDmXdpi(newXdpi);
    return resources;
  }

  public static Resources adaptHeight(Resources resources, int designHeight) {
    DisplayMetrics dm = getDisplayMetrics(resources);
    float newXdpi = dm.xdpi = (dm.heightPixels * 72f) / designHeight;
    setAppDmXdpi(newXdpi);
    return resources;
  }

  private static void setAppDmXdpi(final float xdpi) {
    Utils.getApp().getResources().getDisplayMetrics().xdpi = xdpi;
  }

  private static DisplayMetrics getDisplayMetrics(Resources resources) {
    DisplayMetrics miuiDisplayMetrics = getMiuiTmpMetrics(resources);
    if (miuiDisplayMetrics == null) {
      return resources.getDisplayMetrics();
    }
    return miuiDisplayMetrics;
  }

  private static DisplayMetrics getMiuiTmpMetrics(Resources resources) {
    if (!isInitMiui) {
      DisplayMetrics ret = null;
      String simpleName = resources.getClass().getSimpleName();
      if ("MiuiResources".equals(simpleName) || "XResources".equals(simpleName)) {
        try {
          //noinspection JavaReflectionMemberAccess
          mTmpMetricsField = Resources.class.getDeclaredField("mTmpMetrics");
          mTmpMetricsField.setAccessible(true);
          ret = (DisplayMetrics) mTmpMetricsField.get(resources);
        } catch (Exception e) {
          Log.e("AdaptScreenUtils", "no field of mTmpMetrics in resources.");
        }
      }
      isInitMiui = true;
      return ret;
    }
    if (mTmpMetricsField == null) {
      return null;
    }
    try {
      return (DisplayMetrics) mTmpMetricsField.get(resources);
    } catch (Exception e) {
      return null;
    }
  }
}
