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

import java.util.Date;

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;
import com.github.androidpirate.slicknotes.repo.NoteRepository;

import androidx.lifecycle.LiveData;

public class NoteDetailViewModel extends BaseNoteViewModel {

    public NoteDetailViewModel(NoteRepository noteRepository) {
        repo = noteRepository;
    }

    public LiveData<NoteWithLabels> getDatabaseNote(int noteId) {
        uiModel = repo.getDatabaseNote(noteId);
        return uiModel;
    }

    public void setDatabaseModel(Note note) {
        this.databaseModel = note;
    }

    public void moveNoteToTrash() {
        databaseModel.setPinned(false);
        databaseModel.setTrash(true);
    }

    public void restoreNote() {
        databaseModel.setTrash(false);
    }

    public void deleteNote() {
        repo.deleteDatabaseNote(databaseModel);
    }

    public void updateNote() {
        updateDatabaseModel();
        repo.updateDatabaseNote(databaseModel);
    }

    private void updateDatabaseModel() {
        databaseModel.setDateEdited(new Date());
    }
}
