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
import androidx.lifecycle.MutableLiveData;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repo;
    private MutableLiveData<Note> note;

    public NoteViewModel(@NonNull Application application, NoteRepository repo) {
        super(application);
        this.repo = repo;
        note = new MutableLiveData<>();
    }

    public LiveData<Note> getDatabaseNote(int noteId) {
        note.setValue(repo.getDatabaseNote(noteId).getValue());
        return note;
    }

    public void updateNotePinStatus(Note note, boolean status) {
        note.setPinned(status);
        updateNote(note);
    }

    public void updateNoteColor(Note note, int colorId) {
        note.setColorId(colorId);
        updateNote(note);
    }

    public void updateNoteLabels(Note note, List<String> labels) {
        note.setLabels(labels);
        updateNote(note);
    }

    public void updateNoteCreateDate(Note note, Date dateCreated) {
        note.setDateCreated(dateCreated);
        updateNote(note);
    }

    public void updateNoteEditDate(Note note, Date dateEdited) {
        note.setDateEdited(dateEdited);
        updateNote(note);
    }

    public void moveNoteToTrash(Note note) {
        note.setTrash(true);
        updateNote(note);
    }

    public void insertDefaultNote(String title, String detail) {
        Note note = new Note(title, detail, new Date());
        repo.insertDatabaseNote(note);
    }

    private void updateNote(Note note) {
        repo.updateDatabaseNote(note);
    }
}
