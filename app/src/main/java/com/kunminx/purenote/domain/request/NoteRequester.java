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
   *  ~
   *  ~
   *  As the 'only credible source', it receives messages sent from the page,
   *  processes the business logic internally, and distributes them through sendResult results.
   *  ~
   *  At the same time, as the adult stage of Single Source of Truth,
   *  automatically eliminates the high-frequency pain spots of "mutable boilerplate code
   *  & Livedata serial event coverage & mutableLiveData.setValue abuse".
   */
  @Override
  protected void onHandle(NoteEvent event) {
    switch (event.eventId) {
      case NoteEvent.EVENT_GET_NOTE_LIST:
        DataRepository.getInstance().getNotes(dataResult -> {
          sendResult(event.copy(new NoteEvent.Result(dataResult.getResult())));
        });
        break;
      case NoteEvent.EVENT_UPDATE_ITEM:
      case NoteEvent.EVENT_MARK_ITEM:
        DataRepository.getInstance().updateNote(event.param.note, dataResult -> {
          sendResult(event.copy(new NoteEvent.Result(dataResult.getResult())));
        });
        break;
      case NoteEvent.EVENT_TOPPING_ITEM:
        DataRepository.getInstance().updateNote(event.param.note, dataResult -> {
          if (dataResult.getResult()) {
            DataRepository.getInstance().getNotes(dataResult1 -> {
              sendResult(event.copy(new NoteEvent.Result(dataResult1.getResult())));
            });
          }
        });
        break;
      case NoteEvent.EVENT_ADD_ITEM:
        DataRepository.getInstance().insertNote(event.param.note, dataResult -> {
          sendResult(event.copy(new NoteEvent.Result(dataResult.getResult())));
        });
        break;
      case NoteEvent.EVENT_REMOVE_ITEM:
        DataRepository.getInstance().deleteNote(event.param.note, dataResult -> {
          sendResult(event.copy(new NoteEvent.Result(dataResult.getResult())));
        });
        break;
    }
  }
}
