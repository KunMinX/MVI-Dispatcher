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

package com.kunminx.purenote.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.kunminx.purenote.data.bean.Note;

/**
 * Create by KunMinX at 2020/7/19
 */
public class DiffUtils {

  private DiffUtil.ItemCallback<Note> mNoteItemCallback;

  private DiffUtils() {
  }

  private static final DiffUtils S_DIFF_UTILS = new DiffUtils();

  public static DiffUtils getInstance() {
    return S_DIFF_UTILS;
  }

  public DiffUtil.ItemCallback<Note> getNoteItemCallback() {
    if (mNoteItemCallback == null) {
      mNoteItemCallback = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
          return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
          return oldItem.getModifyDate().equals(newItem.getModifyDate());
        }
      };
    }
    return mNoteItemCallback;
  }
}
