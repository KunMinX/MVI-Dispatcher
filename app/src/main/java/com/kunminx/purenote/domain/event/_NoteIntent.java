package com.kunminx.purenote.domain.event;

import com.kunminx.purenote.data.bean.Note;
import com.kunminx.sealed.annotation.Param;
import com.kunminx.sealed.annotation.SealedClass;

import java.util.List;
/**
 * TODO：可用于 Java 8 的 Sealed Class，使用方式见：
 * https://github.com/KunMinX/Java8-Sealed-Class
 *
 * Create by KunMinX at 2022/8/30
 */
@SealedClass
public interface _NoteIntent {
  void getNoteList(List<Note> notes);
  void removeItem(@Param Note note, boolean isSuccess);
  void updateItem(@Param Note note, boolean isSuccess);
  void markItem(@Param Note note, boolean isSuccess);
  void toppingItem(@Param Note note, boolean isSuccess);
  void addItem(@Param Note note, boolean isSuccess);
}
