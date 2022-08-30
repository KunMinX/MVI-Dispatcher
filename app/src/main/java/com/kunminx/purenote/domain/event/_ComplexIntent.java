package com.kunminx.purenote.domain.event;

import com.kunminx.sealed.annotation.Param;
import com.kunminx.sealed.annotation.SealedClass;
/**
 * TODO：可用于 Java 1.8 的 Sealed Class，使用方式见：
 * https://github.com/KunMinX/SealedClass4Java
 *
 * Create by KunMinX at 2022/8/30
 */
@SealedClass
public interface _ComplexIntent {
  void test1(@Param int count, int count1);
  void test2(@Param int count, int count1);
  void test3(@Param int count, int count1);
  void test4(@Param int count, int count1);
}
