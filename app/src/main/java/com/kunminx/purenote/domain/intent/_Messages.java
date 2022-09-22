package com.kunminx.purenote.domain.intent;

import com.kunminx.sealed.annotation.SealedClass;
/**
 * TODO：可用于 Java 1.8 的 Sealed Class，使用方式见：
 * https://github.com/KunMinX/SealedClass4Java
 *
 * Create by KunMinX at 2022/8/30
 */
@SealedClass
public interface _Messages {
  void refreshNoteList();
  void finishActivity();
}
