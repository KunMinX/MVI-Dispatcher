package com.kunminx.purenote.data.repo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kunminx.purenote.data.bean.Note;

import java.util.List;

/**
 * Create by KunMinX at 2022/6/14
 */
@Dao
public interface NoteDao {

  @Query("select * from note order by type & 0x0001 = 0x0001 desc, modify_time desc")
  List<Note> getNotes();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertNote(Note note);

  @Update()
  void updateNote(Note note);

  @Delete
  void deleteNote(Note note);

}
