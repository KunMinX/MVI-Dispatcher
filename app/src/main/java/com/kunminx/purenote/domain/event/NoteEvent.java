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
    super(eventId);
  }

  public NoteEvent(int eventId, Param param) {
    super(eventId, param);
  }

  public NoteEvent(int eventId, Param param, Result result) {
    super(eventId, param, result);
  }

  public static class Param {
    public final Note note;
    public Param(Note note) {
      this.note = note;
    }
  }

  public static class Result {
    public final List<Note> notes;
    public final boolean isSuccess;

    public Result(List<Note> notes) {
      this.notes = notes;
      this.isSuccess = true;
    }
    public Result(boolean isSuccess) {
      this.notes = null;
      this.isSuccess = isSuccess;
    }
  }

  public static NoteEvent addNote(Note note) {
    return new NoteEvent(NoteEvent.EVENT_ADD_ITEM, new Param(note));
  }

  public static NoteEvent markNote(Note note) {
    return new NoteEvent(NoteEvent.EVENT_MARK_ITEM, new Param(note));
  }

  public static NoteEvent toppingNote(Note note) {
    return new NoteEvent(NoteEvent.EVENT_TOPPING_ITEM, new Param(note));
  }

  public static NoteEvent removeNote(Note note) {
    return new NoteEvent(NoteEvent.EVENT_REMOVE_ITEM, new Param(note));
  }

  public static NoteEvent getList() {
    return new NoteEvent(NoteEvent.EVENT_GET_NOTE_LIST);
  }

  public NoteEvent copy(NoteEvent.Result result) {
    return new NoteEvent(this.eventId, this.param, result);
  }
}
