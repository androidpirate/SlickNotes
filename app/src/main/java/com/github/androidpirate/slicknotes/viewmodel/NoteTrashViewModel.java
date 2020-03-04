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

import androidx.lifecycle.LiveData;

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.repo.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class NoteTrashViewModel extends BaseListViewModel {

    public NoteTrashViewModel(NoteRepository noteRepository) {
        repo = noteRepository;
        selectedNotes = new ArrayList<>();
        selectedNoteIds = new ArrayList<>();
        uiModel = repo.getDatabaseTrashNotes();
    }

    public LiveData<List<Note>> getDatabaseTrashNotes() {
        return repo.getDatabaseTrashNotes();
    }

    public void deleteNotes(List<Note> notes) {
        repo.deleteDatabaseNotes(notes);
    }

    public void restoreNotes(List<Note> notes) {
        databaseModel = new ArrayList<>(notes);
        for (Note note: databaseModel) {
            note.setTrash(false);
        }
        repo.updateDatabaseNotes(databaseModel);
    }

}
