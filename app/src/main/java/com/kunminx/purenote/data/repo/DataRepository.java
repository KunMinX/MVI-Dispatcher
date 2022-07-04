package com.kunminx.purenote.data.repo;

import androidx.room.Room;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.purenote.data.bean.Note;
import com.kunminx.purenote.data.response.AsyncTask;

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

  public void getNotes(DataResult.Result<List<Note>> result) {
    AsyncTask.doAction(() -> mDataBase.noteDao().getNotes(),
            notes -> result.onResult(new DataResult<>(notes)));
  }

  public void insertNote(Note note, DataResult.Result<Boolean> result) {
    AsyncTask.doAction(() -> {
      mDataBase.noteDao().insertNote(note);
      return true;
    }, success -> result.onResult(new DataResult<>(success)));
  }

  public void updateNote(Note note, DataResult.Result<Boolean> result) {
    AsyncTask.doAction(() -> {
      mDataBase.noteDao().updateNote(note);
      return true;
    }, success -> result.onResult(new DataResult<>(success)));
  }

  public void deleteNote(Note note, DataResult.Result<Boolean> result) {
    AsyncTask.doAction(() -> {
      mDataBase.noteDao().deleteNote(note);
      return true;
    }, success -> result.onResult(new DataResult<>(success)));
  }
}
