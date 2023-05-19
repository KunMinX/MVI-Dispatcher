package com.kunminx.purenote.domain.request;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.purenote.data.repo.DataRepository;
import com.kunminx.purenote.domain.intent.NoteIntent;

/**
 * Create by KunMinX at 2022/6/14
 */
public class NoteRequester extends MviDispatcher<NoteIntent> {

  /**
   * TODO tip 1：
   *  此为领域层组件，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  可为同业务不同页面复用。
   *  ~
   *  本组件通过封装，默使数据从 "领域层" 到 "表现层" 单向流动，
   *  消除 “mutable 样板代码 + 连发事件覆盖 + mutable.setValue 误用滥用” 等高频痛点。
   */
  @Override
  protected void onHandle(NoteIntent intent) {
    switch (intent.id) {
      case NoteIntent.GetNoteList.ID:
        NoteIntent.GetNoteList getNoteList = (NoteIntent.GetNoteList) intent;
        DataRepository.getInstance().getNotes(dataResult -> {
          sendResult(getNoteList.copy(dataResult.getResult()));
        });
        break;
      case NoteIntent.UpdateItem.ID:
        NoteIntent.UpdateItem updateItem = (NoteIntent.UpdateItem) intent;
        DataRepository.getInstance().updateNote(updateItem.paramNote, dataResult -> {
          sendResult(updateItem.copy(dataResult.getResult()));
        });
        break;
      case NoteIntent.MarkItem.ID:
        NoteIntent.MarkItem markItem = (NoteIntent.MarkItem) intent;
        DataRepository.getInstance().updateNote(markItem.paramNote, dataResult -> {
          sendResult(markItem.copy(dataResult.getResult()));
        });
        break;
      case NoteIntent.ToppingItem.ID:
        NoteIntent.ToppingItem toppingItem = (NoteIntent.ToppingItem) intent;
        DataRepository.getInstance().updateNote(toppingItem.paramNote, dataResult -> {
          if (dataResult.getResult()) {
            DataRepository.getInstance().getNotes(dataResult1 -> {
              sendResult(NoteIntent.GetNoteList(dataResult1.getResult()));
            });
          }
        });
        break;
      case NoteIntent.AddItem.ID:
        NoteIntent.AddItem addItem = (NoteIntent.AddItem) intent;
        DataRepository.getInstance().insertNote(addItem.paramNote, dataResult -> {
          sendResult(addItem.copy(dataResult.getResult()));
        });
        break;
      case NoteIntent.RemoveItem.ID:
        NoteIntent.RemoveItem removeItem = (NoteIntent.RemoveItem) intent;
        DataRepository.getInstance().deleteNote(removeItem.paramNote, dataResult -> {
          sendResult(removeItem.copy(dataResult.getResult()));
        });
        break;
    }
  }
}
