package com.kunminx.purenote.domain.request;

import com.kunminx.architecture.domain.dispatch.TruthDispatcher;
import com.kunminx.purenote.data.repo.DataRepository;
import com.kunminx.purenote.domain.event.NoteListEvent;

/**
 * Create by KunMinX at 2022/6/14
 */
public class NoteRequester extends TruthDispatcher<NoteListEvent> {

  @Override
  public void input(NoteListEvent event) {
    super.input(event);

    switch (event.eventId) {
      case NoteListEvent.EVENT_GET_NOTE_LIST:
        DataRepository.getInstance().getNotes(dataResult -> {
          event.result.notes = dataResult.getResult();
          sendResult(event);
        });
        break;
      case NoteListEvent.EVENT_UPDATE_ITEM:
      case NoteListEvent.EVENT_MARK_ITEM:
        DataRepository.getInstance().updateNote(event.param.note, dataResult -> {
          event.result.isSuccess = dataResult.getResult();
          sendResult(event);
        });
        break;
      case NoteListEvent.EVENT_TOPPING_ITEM:
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
      case NoteListEvent.EVENT_ADD_ITEM:
        DataRepository.getInstance().insertNote(event.param.note, dataResult -> {
          event.result.isSuccess = dataResult.getResult();
          sendResult(event);
        });
        break;
      case NoteListEvent.EVENT_REMOVE_ITEM:
        DataRepository.getInstance().deleteNote(event.param.note, dataResult -> {
          event.result.isSuccess = dataResult.getResult();
          sendResult(event);
        });
        break;
    }
  }
}
