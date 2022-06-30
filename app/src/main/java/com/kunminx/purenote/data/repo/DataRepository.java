package com.kunminx.purenote.data.repo;

import androidx.room.Room;

import com.kunminx.architecture.utils.Utils;
import com.kunminx.purenote.data.bean.Note;

import java.util.List;

/**
 * Create by KunMinX at 2022/6/14
 */
public class DataRepository {

  private static final DataRepository instance = new DataRepository();
  private static final String DATABASE_NAME = "NOTE_DB.db";
  private final NoteDataBase mDataBase;

  public static DataRepository getInstance() {
    return instance;
  }

  private DataRepository() {
    mDataBase = Room.databaseBuilder(Utils.getApp().getApplicationContext(), NoteDataBase.class, DATABASE_NAME).build();
  }

  public List<Note> getNotes() {
    return mDataBase.noteDao().getNotes();
  }

  public void insertNote(Note note) {
    mDataBase.noteDao().insertNote(note);
  }

  public void updateNote(Note note) {
    mDataBase.noteDao().updateNote(note);
  }

  public void deleteNote(Note note) {
    mDataBase.noteDao().deleteNote(note);
  }
}
