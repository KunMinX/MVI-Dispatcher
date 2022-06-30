package com.kunminx.purenote.domain.request;

import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.domain.message.MutableResult;
import com.kunminx.architecture.domain.message.Result;
import com.kunminx.purenote.data.repo.DataRepository;
import com.kunminx.purenote.domain.event.NoteListEvent;

/**
 * Create by KunMinX at 2022/6/14
 */
public class NoteRequester extends ViewModel {

  private final MutableResult<NoteListEvent> noteListResult = new MutableResult<>();

  public Result<NoteListEvent> getNoteListResult() {
    return noteListResult;
  }

  public void requestNoteList(NoteListEvent noteListEvent) {
    switch (noteListEvent.eventId) {
      case NoteListEvent.EVENT_GET_NOTE_LIST:
        DataRepository.getInstance().getNotes(dataResult -> {
          noteListEvent.result.notes = dataResult.getResult();
          noteListResult.setValue(noteListEvent);
        });
        break;
      case NoteListEvent.EVENT_UPDATE_ITEM:
      case NoteListEvent.EVENT_MARK_ITEM:
        DataRepository.getInstance().updateNote(noteListEvent.param.note, dataResult -> {
          noteListEvent.result.isSuccess = dataResult.getResult();
          noteListResult.setValue(noteListEvent);
        });
        break;
      case NoteListEvent.EVENT_TOPPING_ITEM:
        DataRepository.getInstance().updateNote(noteListEvent.param.note, dataResult -> {
          noteListEvent.result.isSuccess = dataResult.getResult();
          if (noteListEvent.result.isSuccess) {
            DataRepository.getInstance().getNotes(dataResult1 -> {
              noteListEvent.result.notes = dataResult1.getResult();
              noteListResult.setValue(noteListEvent);
            });
          }
        });
        break;
      case NoteListEvent.EVENT_ADD_ITEM:
        DataRepository.getInstance().insertNote(noteListEvent.param.note, dataResult -> {
          noteListEvent.result.isSuccess = dataResult.getResult();
          noteListResult.setValue(noteListEvent);
        });
        break;
      case NoteListEvent.EVENT_REMOVE_ITEM:
        DataRepository.getInstance().deleteNote(noteListEvent.param.note, dataResult -> {
          noteListEvent.result.isSuccess = dataResult.getResult();
          noteListResult.setValue(noteListEvent);
        });
        break;
    }
  }
}
