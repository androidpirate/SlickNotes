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
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.repo.NoteRepository;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NoteViewModel extends AndroidViewModel {
    private static final String DEFAULT_NOTE_TITLE = "";
    private static final String DEFAULT_NOTE_DETAILS = "";
    private NoteRepository repo;
    private LiveData<Note> uiModel;
    private Note databaseModel;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repo = new NoteRepository(application);
        initialize();
    }

    public LiveData<Note> getDatabaseNote(int noteId) {
        uiModel = repo.getDatabaseNote(noteId);
        return uiModel;
    }

    public void updateDatabaseNote(Note note) {
        this.databaseModel = note;
    }

    public boolean updateNotePinStatus() {
        databaseModel.setPinned(!databaseModel.isPinned());
        return databaseModel.isPinned();
    }

    public void updateNoteColor(int colorId) {
        databaseModel.setColorId(colorId);
    }

    public void updateNoteLabels(List<String> labels) {
        databaseModel.setLabels(labels);
    }

    public void updateNoteCreateDate(Date dateCreated) {
        databaseModel.setDateCreated(dateCreated);
    }

    public void updateNoteEditDate(Date dateEdited) {
        databaseModel.setDateEdited(dateEdited);
    }

    public void moveNoteToTrash() {
        databaseModel.setTrash(true);
    }

    public void updateNote(String title, String details) {
        databaseModel.setTitle(title);
        databaseModel.setDetails(details);
        if(checkDatabaseModelIsEmpty()) {
            moveNoteToTrash();
            repo.updateDatabaseNote(databaseModel);
            displayEmptyNoteDiscardedToast();
        } else {
            repo.updateDatabaseNote(databaseModel);
        }
    }

    private void initialize() {
        databaseModel = new Note(DEFAULT_NOTE_TITLE, DEFAULT_NOTE_DETAILS, new Date());
    }

    /**
     * NoteCreateFragment methods
     */
    public void insertNote(String title, String details) {
        databaseModel.setTitle(title);
        databaseModel.setDetails(details);
        if(checkDatabaseModelIsEmpty()) {
            displayEmptyNoteDiscardedToast();
        } else {
            repo.insertDatabaseNote(databaseModel);
        }
    }

    private void displayEmptyNoteDiscardedToast() {
        Toast.makeText(getApplication(), "Empty note is discarded.", Toast.LENGTH_SHORT).show();
    }

    private boolean checkDatabaseModelIsEmpty() {
        return databaseModel.getTitle().isEmpty() && databaseModel.getDetails().isEmpty();
    }
}
