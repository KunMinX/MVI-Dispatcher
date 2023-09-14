package com.kunminx.purenote.domain.intent;

import com.kunminx.sealed.annotation.SealedClass;
/**
 * TODO：可用于 Java 1.8 的 Sealed Class，使用方式见：
 * https://github.com/KunMinX/SealedClass4Java
 *
 * TODO tip 2：此 Intent 非传统意义上的 MVI intent，
 *  而是简化 reduce 和 action 后，拍平的 intent，
 *  它可以携带 param，经由 input 接口发送至 mvi-Dispatcher，
 *  可以 copy 和携带 result，经由 output 接口回推至表现层，
 *
 *  具体可参见《解决 MVI 实战痛点》解析
 *  https://juejin.cn/post/7134594010642907149
 *
 * Create by KunMinX at 2022/8/30
 */
@SealedClass
public interface _Messages {
  void refreshNoteList();
  void finishActivity();
}
