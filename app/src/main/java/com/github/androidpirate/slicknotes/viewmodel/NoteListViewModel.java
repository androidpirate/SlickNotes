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

package com.github.androidpirate.slicknotes.viewmodel;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.repo.NoteRepository;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NoteListViewModel extends AndroidViewModel {
    private NoteRepository repo;
    private MutableLiveData<List<Note>> databaseNotes;
    private MutableLiveData<List<Note>> trashDatabaseNotes;

    public NoteListViewModel(@NonNull Application application) {
        super(application);
        repo = new NoteRepository(application);
        databaseNotes = new MutableLiveData<>();
        trashDatabaseNotes = new MutableLiveData<>();
    }

    public LiveData<List<Note>> getDatabaseNotes() {
        databaseNotes.setValue(getCombinedNotes());
        return databaseNotes;
    }

    private List<Note> getCombinedNotes() {
        List<Note> combinedNotes = new ArrayList<>();
        combinedNotes.addAll(repo.getPinnedDatabaseNotes());
        combinedNotes.addAll(repo.getNonPinnedDatabaseNotes());
        return combinedNotes;
    }

    public LiveData<List<Note>> getDatabaseTrashNotes() {
        trashDatabaseNotes.setValue(repo.getDatabaseTrashNotes().getValue());
        return trashDatabaseNotes;
    }

    public void moveNotesToTrash(List<Note> notes) {
        for (Note note:
             notes) {
            note.setTrash(true);
        }
        repo.updateDatabaseNotes(notes);
    }

    public void pinNotes(List<Note> notes) {
        for (Note note:
             notes) {
            note.setPinned(true);
        }
        repo.updateDatabaseNotes(notes);
    }

    public void deleteNotes(List<Note> notes) {
        repo.deleteDatabaseNotes(notes);
    }

    public void restoreNotes(List<Note> notes) {
        for (Note note:
             notes) {
            note.setTrash(false);
        }
        repo.updateDatabaseNotes(notes);
    }
}