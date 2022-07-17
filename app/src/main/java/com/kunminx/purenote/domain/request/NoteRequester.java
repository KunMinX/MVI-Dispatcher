package com.kunminx.purenote.domain.request;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.purenote.data.repo.DataRepository;
import com.kunminx.purenote.domain.event.NoteEvent;

/**
 * Create by KunMinX at 2022/6/14
 */
public class NoteRequester extends MviDispatcher<NoteEvent> {

  /**
   * TODO tip 1：
   *  作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  ~
   *  与此同时，作为唯一可信源成熟态，
   *  自动消除 “mutable 样板代码 + LiveData 连发事件覆盖 + LiveData.setValue 误用滥用” 高频痛点。
   */
  @Override
  protected void onInput(NoteEvent event) {
    switch (event.eventId) {
      case NoteEvent.EVENT_GET_NOTE_LIST:
        DataRepository.getInstance().getNotes(dataResult -> {
          event.result.notes = dataResult.getResult();
          sendResult(event);
        });
        break;
      case NoteEvent.EVENT_UPDATE_ITEM:
      case NoteEvent.EVENT_MARK_ITEM:
        DataRepository.getInstance().updateNote(event.param.note, dataResult -> {
          event.result.isSuccess = dataResult.getResult();
          sendResult(event);
        });
        break;
      case NoteEvent.EVENT_TOPPING_ITEM:
        DataRepository.getInstance().updateNote(event.param.note, dataResult -> {
          event.result.isSuccess = dataResult.getResult();
          if (event.result.isSuccess) {
            DataRepository.getInstance().getNotes(dataResult1 -> {
              event.result.notes = dataResult1.getResult();
              sendResult(event);
            });
          }
        });
        break;
      case NoteEvent.EVENT_ADD_ITEM:
        DataRepository.getInstance().insertNote(event.param.note, dataResult -> {
          event.result.isSuccess = dataResult.getResult();
          sendResult(event);
        });
        break;
      case NoteEvent.EVENT_REMOVE_ITEM:
        DataRepository.getInstance().deleteNote(event.param.note, dataResult -> {
          event.result.isSuccess = dataResult.getResult();
          sendResult(event);
        });
        break;
    }
  }
}
