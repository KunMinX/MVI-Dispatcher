package com.kunminx.purenote.domain.event;

import com.kunminx.architecture.domain.event.Event;
import com.kunminx.purenote.data.bean.Note;

import java.util.List;

/**
 * Create by KunMinX at 2022/6/16
 */
public class NoteEvent extends Event<NoteEvent.Param, NoteEvent.Result> {
  public final static int EVENT_GET_NOTE_LIST = 1;
  public final static int EVENT_REMOVE_ITEM = 2;
  public final static int EVENT_UPDATE_ITEM = 3;
  public final static int EVENT_MARK_ITEM = 4;
  public final static int EVENT_TOPPING_ITEM = 5;
  public final static int EVENT_ADD_ITEM = 6;

  public NoteEvent(int eventId) {
    this.eventId = eventId;
    this.param = new Param();
    this.result = new Result();
  }

  public NoteEvent setNote(Note note) {
    this.param.note = note;
    return this;
  }

  public static class Param {
    public Note note;
  }

  public static class Result {
    public List<Note> notes;
    public boolean isSuccess;
  }
}
