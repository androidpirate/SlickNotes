/*
 * <!--
 *  Copyright (C) 2016 The Android Open Source Project
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 * -->
 */

package com.github.androidpirate.slicknotes.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM Note WHERE note_trash_status = 0 " +
            "ORDER BY note_pin_status DESC, note_create_date ASC")
    LiveData<List<Note>> getDatabaseNotesDescending();
    @Query("SELECT * FROM Note WHERE note_trash_status = 0 " +
            "ORDER BY note_pin_status DESC, note_create_date DESC")
    LiveData<List<Note>> getDatabaseNotesAscending();
    @Query("SELECT * FROM Note WHERE noteId = :id")
    LiveData<Note> getDatabaseNote(int id);
    @Query("SELECT * FROM Note WHERE note_trash_status = 1")
    LiveData<List<Note>> getTrashNotes();
    @Query("SELECT * FROM Note WHERE note_pin_status = 1")
    LiveData<List<Note>> getPinnedNotes();
    @Query("SELECT * FROM Note WHERE note_pin_status = 0")
    LiveData<List<Note>> getNonPinnedNotes();
    @Insert
    void insertDatabaseNote(Note note);
    @Delete
    void deleteDatabaseNote(Note note);
    @Delete
    void deleteDatabaseNotes(List<Note> notes);
    @Update
    void updateDatabaseNote(Note note);
    @Update
    void updateDatabaseNotes(List<Note> notes);
    @Query("UPDATE Note SET note_trash_status = :isTrash, note_pin_status = 0 WHERE noteId = :noteId")
    void updateTrashStatus(int noteId, boolean isTrash);
    @Query("UPDATE Note SET note_pin_status = :pinStatus WHERE noteId = :noteId")
    void updatePinStatus(int noteId, boolean pinStatus);
}
