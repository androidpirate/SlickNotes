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
    private Note note;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repo = new NoteRepository(application);
        note = new Note(DEFAULT_NOTE_TITLE, DEFAULT_NOTE_DETAILS, new Date());
    }

    public LiveData<Note> getDatabaseNote(int noteId) {
        return repo.getDatabaseNote(noteId);
    }

    public void updateViewModelNote(Note note) {
        this.note = note;
    }

    public void updateNoteTitle(String title) {
        note.setTitle(title);
    }

    public void updateNoteDetails(String details) {
        note.setDetails(details);
    }

    public void updateNotePinStatus(boolean status) {
        note.setPinned(status);
    }

    public void updateNoteColor(int colorId) {
        note.setColorId(colorId);
    }

    public void updateNoteLabels(List<String> labels) {
        note.setLabels(labels);
    }

    public void updateNoteCreateDate(Date dateCreated) {
        note.setDateCreated(dateCreated);
    }

    public void updateNoteEditDate(Date dateEdited) {
        note.setDateEdited(dateEdited);
    }

    public void moveNoteToTrash() {
        note.setTrash(true);
    }

    public void insertDefaultNote(String title, String detail) {
        Note note = new Note(title, detail, new Date());
        repo.insertDatabaseNote(note);
    }

    public void updateNote() {
        repo.updateDatabaseNote(note);
    }
}
