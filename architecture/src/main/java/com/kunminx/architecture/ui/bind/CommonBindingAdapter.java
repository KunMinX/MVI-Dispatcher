/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.architecture.ui.bind;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.kunminx.architecture.utils.Utils;

/**
 * Create by KunMinX at 19/9/18
 */
public class CommonBindingAdapter {
  @BindingAdapter(value = {"visible"}, requireAll = false)
  public static void visible(View view, boolean visible) {
    if (visible && view.getVisibility() == View.GONE) {
      view.setVisibility(View.VISIBLE);
    } else if (!visible && view.getVisibility() == View.VISIBLE) {
      view.setVisibility(View.GONE);
    }
  }

  @BindingAdapter(value = {"invisible"}, requireAll = false)
  public static void invisible(View view, boolean visible) {
    if (visible && view.getVisibility() == View.INVISIBLE) {
      view.setVisibility(View.VISIBLE);
    } else if (!visible && view.getVisibility() == View.VISIBLE) {
      view.setVisibility(View.INVISIBLE);
    }
  }

  @BindingAdapter(value = {"imgRes"}, requireAll = false)
  public static void setImageResource(ImageView imageView, int imgRes) {
    imageView.setImageResource(imgRes);
  }

  @BindingAdapter(value = {"textColor"}, requireAll = false)
  public static void setTextColor(TextView textView, int textColorRes) {
    textView.setTextColor(textView.getContext().getColor(textColorRes));
  }

  @BindingAdapter(value = {"selected"}, requireAll = false)
  public static void selected(View view, boolean select) {
    view.setSelected(select);
  }

  @BindingAdapter(value = {"clipToOutline"}, requireAll = false)
  public static void clipToOutline(View view, boolean clipToOutline) {
    view.setClipToOutline(clipToOutline);
  }

  @BindingAdapter(value = {"requestFocus"}, requireAll = false)
  public static void requestFocus(View view, boolean requestFocus) {
    if (requestFocus) view.requestFocus();
  }

  @BindingAdapter(value = {"showKeyboard"}, requireAll = false)
  public static void showKeyboard(View view, boolean showKeyboard) {
    InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
  }
}
