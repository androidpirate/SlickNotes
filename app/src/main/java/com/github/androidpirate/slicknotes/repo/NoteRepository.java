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

package com.github.androidpirate.slicknotes.repo;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.data.NoteDao;
import com.github.androidpirate.slicknotes.data.NoteDatabase;
import androidx.lifecycle.LiveData;

public class NoteRepository {
    private NoteDao dao;
    private Executor executor;

    public NoteRepository(Context context) {
        this.dao = NoteDatabase.getInstance(context).dao();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Note>> getDatabaseNotes() {
        return dao.getDatabaseNotes();
    }

    public LiveData<List<Note>> getDatabaseTrashNotes() {
        return dao.getTrashNotes();
    }

    public List<Note> getPinnedDatabaseNotes() {
        return dao.getPinnedNotes();
    }

    public List<Note> getNonPinnedDatabaseNotes() {
        return dao.getNonPinnedNotes();
    }

    public LiveData<Note> getDatabaseNote(int noteId) {
        return dao.getDatabaseNote(noteId);
    }

    public void insertDatabaseNote(final Note note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertDatabaseNote(note);
            }
        });
    }

    public void deleteDatabaseNote(final Note note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteDatabaseNote(note);
            }
        });
    }

    public void deleteDatabaseNotes(final List<Note> notes) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteDatabaseNotes(notes);
            }
        });
    }

    public void updateDatabaseNote(final Note note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateDatabaseNote(note);
            }
        });
    }

    public void updateDatabaseNotes(final List<Note> notes) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateDatabaseNotes(notes);
            }
        });
    }
}
