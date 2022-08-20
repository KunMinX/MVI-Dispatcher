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

package com.kunminx.architecture.ui.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kunminx.architecture.ui.scope.ViewModelScope;

/**
 * Create by KunMinX at 19/7/11
 */
public abstract class BaseFragment extends DataBindingFragment {

  private final ViewModelScope mViewModelScope = new ViewModelScope();
  protected AppCompatActivity mActivity;

  protected void onInitData() {
  }

  protected void onOutput() {
  }

  protected void onInput() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    mActivity = (AppCompatActivity) context;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addOnBackPressed();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    onInitData();
    onOutput();
    onInput();
  }

  //TODO tip 2: Jetpack 通过 "工厂模式" 实现 ViewModel 作用域可控，
  //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
  //值得注意的是，通过不同作用域 Provider 获得 ViewModel 实例非同一个，
  //故若 ViewModel 状态信息保留不符合预期，可从该角度出发排查 是否眼前 ViewModel 实例非目标实例所致。

  //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6257931840

  protected <T extends ViewModel> T getFragmentScopeViewModel(@NonNull Class<T> modelClass) {
    return mViewModelScope.getFragmentScopeViewModel(this, modelClass);
  }

  protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
    return mViewModelScope.getActivityScopeViewModel(mActivity, modelClass);
  }

  protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
    return mViewModelScope.getApplicationScopeViewModel(modelClass);
  }

  protected NavController nav() {
    return NavHostFragment.findNavController(this);
  }

  protected void toggleSoftInput() {
    InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  protected void showKeyboard() {
    if (mActivity != null) mActivity.getWindow().getDecorView().post(this::toggleSoftInput);
  }

  protected void openUrlInBrowser(String url) {
    Uri uri = Uri.parse(url);
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    startActivity(intent);
  }

  protected Context getAppContext() {
    return mActivity.getApplicationContext();
  }

  private void addOnBackPressed() {
    requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        if (!onBackPressed()) requireActivity().getOnBackPressedDispatcher().onBackPressed();
      }
    });
  }

  protected boolean onBackPressed() {
    return true;
  }
}
