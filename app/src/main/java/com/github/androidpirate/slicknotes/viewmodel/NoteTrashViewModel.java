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
import com.github.androidpirate.slicknotes.data.NoteWithLabels;
import com.github.androidpirate.slicknotes.repo.NoteRepository;

import java.util.List;

public class NoteTrashViewModel extends BaseListViewModel {

    public NoteTrashViewModel(NoteRepository noteRepository) {
        repo = noteRepository;
    }

    public LiveData<List<NoteWithLabels>> getDatabaseTrashNotes() {
        uiModel = repo.getDatabaseTrashNotes();
        return uiModel;
    }

    public void deleteNotes(List<Note> notes) {
        repo.deleteDatabaseNotes(notes);
    }

    public void restoreNotes(List<Note> notes) {
        for (Note note: notes) {
            repo.updateNoteTrashStatus(note.getNoteId(), false);
        }
    }
}
